package com.guohuai.asset.manage.boot.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentDao
		extends JpaRepository<Investment, String>, JpaSpecificationExecutor<Investment> {

}
