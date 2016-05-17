package com.guohuai.asset.manage.boot.duration.order.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundPurchaseDao extends JpaRepository<FundPurchaseEntity, String>, JpaSpecificationExecutor<FundPurchaseEntity> {

}
