package com.guohuai.asset.manage.boot.investor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestorHoldingDao extends JpaRepository<InvestorHolding, String>, JpaSpecificationExecutor<InvestorHolding> {
	

}
