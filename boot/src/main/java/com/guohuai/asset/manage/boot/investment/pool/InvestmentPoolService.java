package com.guohuai.asset.manage.boot.investment.pool;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.enums.TargetEventType;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentDao;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.boot.investment.log.InvestmentLogService;
import com.guohuai.asset.manage.boot.investment.manage.InvestmentManageForm;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
@Transactional
public class InvestmentPoolService {
	@Autowired
	InvestmentService investmentService;
	@Autowired
	TargetOverdueService targetOverdueService;

	@Autowired
	private InvestmentDao investmentDao;
	
	@Autowired
	private TargetOverdueDao targetOverdueDao;
	
	@Autowired
	InvestmentLogService investmentLogService;

	/**
	 * 获得投资标的列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<Investment> getInvestmentList(Specification<Investment> spec, Pageable pageable) {
		return investmentDao.findAll(spec, pageable);
	}

	/**
	 * 获得投资标的详情
	 * 
	 * @param oid
	 * @return
	 */
	public Investment getInvestmentDet(String oid) {
		return investmentDao.findOne(oid);
	}

	/**
	 * 新建投资标的
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public Investment saveInvestment(Investment entity, String operator) {
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentDao.save(entity);
	}

	/**
	 * 修改投资标的
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public Investment updateInvestment(Investment entity, String operator) {
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentDao.save(entity);
	}

	/**
	 * form转entity
	 * 
	 * @param form
	 * @return
	 */
	public Investment createInvestment(InvestmentManageForm form) {
		Investment entity = new Investment();
		if (StringUtils.isEmpty(form.getOid())) {
			entity.setOid(StringUtil.uuid());
		}
		try {
			BeanUtils.copyProperties(form, entity);
			// 资产规模 万转元
			if (form.getRaiseScope() != null) {
				BigDecimal yuan = form.getRaiseScope().multiply(new BigDecimal(10000));
				entity.setRaiseScope(yuan);
			}
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 标的成立
	 * 
	 * @Title: establish
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param form
	 * @return Investment 返回类型
	 */
	public Investment establish(EstablishForm form) {
		String oid = form.getOid();
		Investment it = investmentService.getInvestment(oid);
		BeanUtils.copyProperties(form, it);
		it.setUpdateTime(DateUtil.getSqlCurrentDate());
		it.setLifeState(Investment.INVESTMENT_LIFESTATUS_STAND_UP); // 重置为成立

		this.investmentDao.save(it);
		investmentLogService.saveInvestmentLog(it, TargetEventType.establish, form.getOperator()); // 保存标的操作日志
		return it;
	}
	
	/**
	 * 标的不成立
	 * 
	 * @Title: unEstablish
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param form
	 * @return Investment 返回类型
	 */
	public Investment unEstablish(UnEstablishForm form) {
		String oid = form.getOid();
		Investment it = investmentService.getInvestment(oid);
		BeanUtils.copyProperties(form, it);
		it.setUpdateTime(DateUtil.getSqlCurrentDate());
		it.setLifeState(Investment.INVESTMENT_LIFESTATUS_STAND_FAIL); // 重置为成立失败

		this.investmentDao.save(it);
		investmentLogService.saveInvestmentLog(it, TargetEventType.unEstablish, form.getOperator()); // 保存标的操作日志
		return it;
	}

	/**
	 * 标的逾期
	 * 
	 * @Title: overdue
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param form
	 * @return Investment 返回类型
	 */
	public Investment overdue(TargetOverdueForm form) {
		String oid = form.getOid();
		
		Investment it = investmentService.getInvestment(oid);

		it.setUpdateTime(DateUtil.getSqlCurrentDate());
		it.setLifeState(Investment.INVESTMENT_LIFESTATUS_OVER_TIME); // 重置为逾期
		
		TargetOverdue to = new TargetOverdue();
		BeanUtils.copyProperties(form, to);
		to.setInvestment(it);
		to.setCreateTime(DateUtil.getSqlCurrentDate());

		this.investmentDao.save(it); // 修改标的库
		targetOverdueDao.save(to); // 添加逾期对象
		investmentLogService.saveInvestmentLog(it, TargetEventType.overdue, form.getOperator()); // 保存标的操作日志
		return it;
	}
	
	/**
	 * 查询指定募集截止日以前的所有投资标的
	 * @Title: getRecruitment 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param collectEndDate   募集截止日
	 * @return List<Investment>    返回类型
	 */
	public List<Investment> getRecruitment(final Date collectEndDate) {
		if (null == collectEndDate)
			throw AMPException.getException("投资标的ID不能为空"); // 默认为当前时间
		// 募集截止日<当前日期 && lifeState = PREPARE
		Specification<Investment> spec = new Specification<Investment>() {
			
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				
				Expression<String> exp = root.get("lifeState").as(String.class); // 标的生命周期					
				predicate.add(exp.in(new Object[] { Investment.INVESTMENT_LIFESTATUS_PREPARE }));//lifeState = PREPARE
				
				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return investmentDao.findAll(spec);
	}
}
