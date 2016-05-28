package com.guohuai.asset.manage.boot.investment.pool;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.pool.CashToolRevenue;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentDao;
import com.guohuai.asset.manage.component.exception.AMPException;

/**
 * 
 * <p>Title: TargetIncomeService.java</p>    
 * <p>本息兑付Service </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:20
 */
@Service
@Transactional
public class TargetIncomeService {
	@Autowired
	TargetIncomeDao targetIncomeDao;
	
	@Autowired
	private InvestmentDao investmentDao;

	/**
	 * 投资标的本息兑付
	 * @Title: save 
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param interestForm
	 * @return Interest    返回类型
	 */
	public TargetIncome save(TargetIncomeForm interestForm) {
		String targetOid = interestForm.getTargetOid();
		if (StringUtils.isBlank(targetOid))
			throw AMPException.getException("投资标的id不能为空");
		
		TargetIncome interest = new TargetIncome();
		BeanUtils.copyProperties(interestForm, interest);		

		Investment investment = this.investmentDao.findOne(targetOid);
		if (null == investment)
			throw AMPException.getException("找不到id为[" + targetOid + "]的投资标的");
		interest.setInvestment(investment);
		
		// 保存之前先删除标的某一期本兮兑付的数据
		targetIncomeDao.deleteByTargetOidAndSeq(targetOid, interestForm.getSeq());
		
		interest.setCreateTime(new Timestamp(System.currentTimeMillis()));
		interest.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		return targetIncomeDao.save(interest);
	}
	

	
	/**
	 * 分页查询本息兑付
	 * 
	 * @Title: getTargetIncomeList
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param spec
	 * @param pageable
	 * @return
	 * @return Page<TargetIncome> 返回类型
	 */
	public Page<TargetIncome> getTargetIncomeList(Specification<TargetIncome> spec, Pageable pageable) {
		return targetIncomeDao.findAll(spec, pageable);
	}
	
	/**
	 * 根据标的id查询所有的本息兑付数据
	 * @Title: findByTargetOidOrderBySeq 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param targetOid
	 * @return List<TargetIncome>    返回类型
	 */
	public List<TargetIncome> findByTargetOidOrderBySeq(String targetOid) {
		return this.targetIncomeDao.findByTargetOidOrderBySeq(targetOid);
	}
}
