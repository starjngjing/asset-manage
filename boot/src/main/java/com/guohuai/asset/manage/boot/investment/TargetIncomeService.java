package com.guohuai.asset.manage.boot.investment;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.investment.pool.TargetIncomeForm;
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

		interest.setCreateTime(new Timestamp(System.currentTimeMillis()));
		interest.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		return targetIncomeDao.save(interest);
	}
}
