package com.guohuai.asset.manage.boot.investment;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.enums.TargetEventType;
import com.guohuai.asset.manage.boot.investment.log.InvestmentLogService;
import com.guohuai.asset.manage.boot.investment.manage.InvestmentManageForm;
import com.guohuai.asset.manage.boot.investment.pool.EstablishForm;
import com.guohuai.asset.manage.boot.investment.pool.UnEstablishForm;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
@Transactional
public class InvestmentService {

	@Autowired
	private InvestmentDao investmentDao;
	
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
		Investment it = getInvestment(oid);
		BeanUtils.copyProperties(form, it);
		it.setState(Investment.INVESTMENT_STATUS_establish); // 重置为成立

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
		Investment it = getInvestment(oid);
		BeanUtils.copyProperties(form, it);
		it.setState(Investment.INVESTMENT_STATUS_unEstablish); // 重置为成立

		this.investmentDao.save(it);
		investmentLogService.saveInvestmentLog(it, TargetEventType.unEstablish, form.getOperator()); // 保存标的操作日志
		return it;
	}

	public Investment getInvestment(String oid) {
		if (null == oid)
			throw AMPException.getException("投资标的ID不能为空");
		Investment old = this.investmentDao.findOne(oid);
		if (null == old)
			throw AMPException.getException("未知的投资标的ID");
		return old;
	}
}
