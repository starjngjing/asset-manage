package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;

@Service
@Transactional
public class InvestmentService {

	@Autowired
	private InvestmentDao investmentDao;

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
	 * 标的成立
	 * @Title: establish 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param entity
	 * @param operator
	 * @return Investment    返回类型
	 */
	public Investment establish(Investment entity, String operator) {
		Investment old = this.investmentDao.findOne(entity.getOid());
		if(null == old)
			throw AMPException.getException("未知的投资标的ID");
		BeanUtils.copyProperties(entity, old);
		this.investmentDao.save(old);
		return entity;
	}
}
