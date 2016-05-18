package com.guohuai.asset.manage.boot.duration.order.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TargetPurchaseDao extends JpaRepository<TargetPurchaseEntity, String>, JpaSpecificationExecutor<TargetPurchaseEntity> {

	public TargetPurchaseEntity findByOid(String oid);
}
