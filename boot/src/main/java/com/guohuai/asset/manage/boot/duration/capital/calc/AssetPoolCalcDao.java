package com.guohuai.asset.manage.boot.duration.capital.calc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AssetPoolCalcDao extends JpaRepository<AssetPoolCalc, String>, JpaSpecificationExecutor<AssetPoolCalc> {

	
}
