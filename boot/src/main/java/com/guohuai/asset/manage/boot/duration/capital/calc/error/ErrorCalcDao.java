package com.guohuai.asset.manage.boot.duration.capital.calc.error;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ErrorCalcDao extends JpaRepository<ErrorCalc, String>, JpaSpecificationExecutor<ErrorCalc> {

	
}
