package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrustTransDao extends JpaRepository<TrustTransEntity, String>, JpaSpecificationExecutor<TrustTransEntity> {

	public TrustTransEntity findByOid(String oid);
}
