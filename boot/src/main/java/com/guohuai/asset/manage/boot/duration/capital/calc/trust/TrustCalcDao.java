package com.guohuai.asset.manage.boot.duration.capital.calc.trust;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrustCalcDao extends JpaRepository<TrustCalc, String>, JpaSpecificationExecutor<TrustCalc> {

	
}
