package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrustPurchaseDao extends JpaRepository<TrustPurchaseEntity, String>, JpaSpecificationExecutor<TrustPurchaseEntity> {

	public TrustPurchaseEntity findByOid(String oid);
}
