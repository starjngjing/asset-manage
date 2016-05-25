package com.guohuai.asset.manage.boot.investment;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.guohuai.asset.manage.boot.investment.log.InvestmentLogService;
import com.guohuai.asset.manage.boot.investment.manage.InvestmentManageForm;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.collect.RiskIndicateCollectForm;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.collect.RiskIndicateCollectResp;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.collect.RiskIndicateCollectService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class InvestmentService {

	@Autowired
	private InvestmentDao investmentDao;

	@Autowired
	InvestmentLogService investmentLogService;
	@Autowired
	RiskIndicateCollectService riskIndicateCollectService;

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
	@Deprecated
	public Investment saveInvestment(Investment entity, String operator) {
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentDao.save(entity);
	}
	/**
	 * 新建投资标的
	 * @Title: saveInvestment 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param form
	 * @return Investment    返回类型
	 */
	public Investment saveInvestment(InvestmentManageForm form) {
		Investment entity = createInvestment(form);
		entity.setState(Investment.INVESTMENT_STATUS_waitPretrial);
		
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		String riskOption = form.getRiskOption(); // 风险配置对象
		if (StringUtils.isNotBlank(riskOption)) {
			RiskIndicateCollectForm collForm = JSON.parseObject(riskOption, RiskIndicateCollectForm.class);
			collForm.setRelative(entity.getOid()); // 把投资标的的oid存入relative
			List<RiskIndicateCollectResp> list = riskIndicateCollectService.save(collForm);
			/* 计算标的风险开始 */
			// TODO
			// 计算公式: sum(CollectScore)
			if (null != list) {
				int sum = 0;
				for (RiskIndicateCollectResp rc : list) {
					sum += rc.getCollectScore();
				}
				log.info("投资标的的风险总分: " + sum);
			}
			/* 计算标的风险结束 */
		}
		
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

	public Investment getInvestment(String oid) {
		if (null == oid)
			throw AMPException.getException("投资标的ID不能为空");
		Investment old = this.investmentDao.findOne(oid);
		if (null == old)
			throw AMPException.getException("未知的投资标的ID");
		return old;
	}

	/**
	 * 标的预审
	 * 
	 * @param oid
	 * @param state
	 * @param operator
	 * @param suggest
	 */
	public void precheck(String oid, String state, String operator, String suggest) {
		Investment investment = this.getInvestmentDet(oid);
		if (investment == null || !Investment.INVESTMENT_STATUS_pretrial.equals(investment.getState())) {
			throw new RuntimeException();
		}
		investment.setState(state);
		if(!StringUtils.isEmpty(suggest)){
			investment.setRejectDesc(suggest);
		}
		this.updateInvestment(investment, operator);
	}

	/**
	 * 根据名称模糊查询投资标的
	 * 
	 * @Title: getInvestmentByName
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param name
	 * @return List<Object> 返回类型
	 */
	public List<Object> getInvestmentByName(String name) {
		return this.investmentDao.getInvestmentByName(name);
	}
	
}
