package com.guohuai.asset.manage.boot.duration.fact.calibration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MarketAdjustOrderDao extends JpaRepository<MarketAdjustOrderEntity, String>, JpaSpecificationExecutor<MarketAdjustOrderEntity> {

}
