package com.guohuai.asset.manage.boot.duration.capital.calc.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundCalcDao extends JpaRepository<FundCalc, String>, JpaSpecificationExecutor<FundCalc> {

	
}
