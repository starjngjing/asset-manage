package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrustAuditDao extends JpaRepository<TrustAuditEntity, String>, JpaSpecificationExecutor<TrustAuditEntity> {

}
