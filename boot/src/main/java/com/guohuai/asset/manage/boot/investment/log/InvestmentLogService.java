package com.guohuai.asset.manage.boot.investment.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.component.util.DateUtil;

@Service
@Transactional
public class InvestmentLogService {

	@Autowired
	private InvestmentLogDao investmentLogDao;

	public InvestmentLog saveInvestmentLog(Investment investment, String eventType, String operator) {
		InvestmentLog entity = InvestmentLog.builder().investment(investment).eventTime(DateUtil.getSqlCurrentDate())
				.eventType(eventType).operator(operator).build();
		return investmentLogDao.save(entity);
	}
}
