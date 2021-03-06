package com.guohuai.asset.manage.boot.investment.pool;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
	private TargetIncomeDao targetIncomeDao;
	
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
		
		// 投资标的募集期收益 百分比转小数
		if (form.getCollectIncomeRate() != null) {
			BigDecimal decimal = form.getCollectIncomeRate().divide(new BigDecimal(100));
			it.setCollectIncomeRate(decimal);
		}
		
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
		// 投资标的募集期收益 百分比转小数
		if (form.getCollectIncomeRate() != null) {
			BigDecimal decimal = form.getCollectIncomeRate().divide(new BigDecimal(100));
			it.setCollectIncomeRate(decimal);
		}
		it.setUpdateTime(DateUtil.getSqlCurrentDate());
		it.setLifeState(Investment.INVESTMENT_LIFESTATUS_STAND_FAIL); // 重置为成立失败

		this.investmentDao.save(it);
		investmentLogService.saveInvestmentLog(it, TargetEventType.unEstablish, form.getOperator()); // 保存标的操作日志
		return it;
	}
	
	/**
	 * 投资标的本息兑付
	 * @Title: targetIncome 
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param form
	 * @return Interest    返回类型
	 */
	public TargetIncome targetIncome(TargetIncomeForm form) {
		String targetOid = form.getTargetOid();
		if (StringUtils.isBlank(targetOid))
			throw AMPException.getException("投资标的id不能为空");

		TargetIncome interest = new TargetIncome();
		BeanUtils.copyProperties(form, interest);
		// 投资标的募集期收益 百分比转小数
		if (form.getIncomeRate() != null) {
			BigDecimal decimal = form.getIncomeRate().divide(new BigDecimal(100));
			interest.setIncomeRate(decimal);
		}

		Investment investment = investmentService.getInvestment(targetOid);
		interest.setInvestment(investment);

		// 保存之前先删除标的某一期本兮兑付的数据
		targetIncomeDao.deleteByTargetOidAndSeq(targetOid, form.getSeq());

		interest.setCreateTime(new Timestamp(System.currentTimeMillis()));
		interest.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		investmentLogService.saveInvestmentLog(investment, TargetEventType.income, form.getOperator()); // 保存标的操作日志
		return targetIncomeDao.save(interest);
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
	 * 结束标的
	 * @Title: close 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param oid
	 * @param operator 
	 * void    返回类型
	 */
	public void close(String oid, String operator) {
		Investment it = investmentService.getInvestment(oid);
		it.setOperator(operator);
		it.setUpdateTime(DateUtil.getSqlCurrentDate());
		it.setLifeState(Investment.INVESTMENT_LIFESTATUS_CLOSE); // 重置为逾期
		this.investmentDao.save(it); // 修改标的库
		investmentLogService.saveInvestmentLog(it, TargetEventType.close, operator); // 保存标的操作日志
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
	public List<Investment> getCollecting(final Date collectEndDate) {
		if (null == collectEndDate)
			throw AMPException.getException("投资标的ID不能为空"); // 默认为当前时间
		// 募集截止日<当前日期 && lifeState = PREPARE
		Specification<Investment> spec = new Specification<Investment>() {
			
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				
				Expression<String> exp = root.get("lifeState").as(String.class); // 标的生命周期					
				predicate.add(exp.in(new Object[] { Investment.INVESTMENT_LIFESTATUS_PREPARE, Investment.INVESTMENT_LIFESTATUS_STAND_UP }));//lifeState = PREPARE
				
				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return investmentDao.findAll(spec);
	}
	
	
	/**
	 * 查询指定类型的所有投资标的
	 * @Title: getCollecting 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param projectTypes   标的类型数组
	 * @return List<Investment>    返回类型
	 */
	public List<Investment> getCollecting(final String[] projectTypes) {
		Specification<Investment> spec = new Specification<Investment>() {
			
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				if (null != projectTypes && projectTypes.length > 0) {
					Expression<String> expType = root.get("type").as(String.class); // 标的类型
					predicate.add(expType.in(projectTypes));// type =  PREPARE
				}
				
				Expression<String> exp = root.get("lifeState").as(String.class); // 标的生命周期					
				predicate.add(exp.in(new Object[] { Investment.INVESTMENT_LIFESTATUS_PREPARE, Investment.INVESTMENT_LIFESTATUS_STAND_UP }));//lifeState = PREPARE
				
//				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
//				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
//				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return investmentDao.findAll(spec);
	}
	
	/**
	 * 查询尚未成立的标的
	 * @Title: getNotEstablishTarget 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param projectTypes	标的类型数组
	 * @return List<Investment>    返回类型
	 */
	public List<Investment> getNotEstablishTarget(final String[] projectTypes) {

		Specification<Investment> spec = new Specification<Investment>() {
			
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				if (null != projectTypes && projectTypes.length > 0) {
					Expression<String> expType = root.get("type").as(String.class); // 标的类型
					predicate.add(expType.in(projectTypes));// type =  PREPARE
				}
				
				Expression<String> exp = root.get("lifeState").as(String.class); // 标的生命周期					
				predicate.add(exp.in(new Object[] { Investment.INVESTMENT_LIFESTATUS_PREPARE}));//lifeState = PREPARE
				
//				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
//				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
//				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return investmentDao.findAll(spec);
	}
	
	/**
	 * 查询已经成立的标的
	 * @Title: getEstablishTarget 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param projectTypes	标的类型数组
	 * @return List<Investment>    返回类型
	 */
	public List<Investment> getEstablishTarget(final String[] projectTypes) {

		Specification<Investment> spec = new Specification<Investment>() {
			
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				if (null != projectTypes && projectTypes.length > 0) {
					Expression<String> expType = root.get("type").as(String.class); // 标的类型
					predicate.add(expType.in(projectTypes));// type =  PREPARE
				}
				
				Expression<String> exp = root.get("lifeState").as(String.class); // 标的生命周期					
				predicate.add(exp.in(new Object[] { Investment.INVESTMENT_LIFESTATUS_STAND_UP}));//lifeState = STAND_UP
				
//				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
//				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
//				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return investmentDao.findAll(spec);
	}

	/**
	 * 根据标的id查询所有的本息兑付数据
	 * 
	 * @Title: getTargetIncome
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param targetOid 投资标的id
	 * @return List<TargetIncome> 返回类型
	 */
	public List<TargetIncome> getTargetIncome(String targetOid) {
		return targetIncomeDao.findByTargetOidOrderBySeq(targetOid);
	}
	
	/**
	 * 增加持仓金额
	 * @Title: IncHoldAmount 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param oid	投资标的id
	 * @param holdAmount	大于0为增加,小于0则为减少
	 * @return Investment    返回类型
	 */
	public Investment IncHoldAmount(String oid, BigDecimal holdAmount) {
		int res = investmentDao.IncHoldAmount(oid, holdAmount);
		return res > 0 ? investmentDao.findOne(oid) : null;
	}
	
	/**
	 * 增加申请金额
	 * @Title: IncApplyAmount 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param oid	投资标的id
	 * @param applyAmount	大于0为增加,小于0则为减少
	 * @return Investment    返回类型
	 */
	public Investment IncApplyAmount(String oid, BigDecimal applyAmount) {
		int res = investmentDao.IncApplyAmount(oid, applyAmount);
		return res > 0 ? investmentDao.findOne(oid) : null;
	}
	
	/**
	 * 检查标的状态
	 * @Title: checkTarget 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param it
	 * @param op
	 *  void    返回类型
	 */
	private void checkTarget(Investment it, String op) {
		String state = it.getState();
		String lifeState = it.getLifeState();
		if (StringUtils.equals(op, Investment.INVESTMENT_LIFESTATUS_STAND_UP)) { // 标的成立
			if (!StringUtils.equals(state, Investment.INVESTMENT_STATUS_collecting) && !StringUtils.equals(lifeState, Investment.INVESTMENT_LIFESTATUS_PREPARE)) {
				throw AMPException.getException("投资标成立失败, 标的状态不合法");
			}
		} else if (StringUtils.equals(op, Investment.INVESTMENT_LIFESTATUS_STAND_FAIL)) { // 标的不成立
			if (!StringUtils.equals(state, Investment.INVESTMENT_STATUS_collecting) && !StringUtils.equals(lifeState, Investment.INVESTMENT_LIFESTATUS_PREPARE)) {
				throw AMPException.getException("投资标不成立失败, 标的状态不合法");
			}
		} else if (StringUtils.equals(op, Investment.INVESTMENT_LIFESTATUS_OVER_TIME)) { // 标的逾期
			if (!StringUtils.equals(state, Investment.INVESTMENT_STATUS_collecting) && !StringUtils.equals(lifeState, Investment.INVESTMENT_LIFESTATUS_STAND_UP)) {
				throw AMPException.getException("投资标逾期失败, 标的状态不合法");
			}
		}
	}
}
