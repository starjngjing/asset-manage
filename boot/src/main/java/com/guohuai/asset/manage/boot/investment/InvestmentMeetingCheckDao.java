package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentMeetingCheckDao
		extends JpaRepository<InvestmentMeetingCheck, String>, JpaSpecificationExecutor<InvestmentMeetingCheck> {

	/**
	 * 根据投资标的查询检查项
	 * 
	 * @param investment
	 * @return
	 */
	public List<InvestmentMeetingCheck> findByInvestment(Investment investment);
}
