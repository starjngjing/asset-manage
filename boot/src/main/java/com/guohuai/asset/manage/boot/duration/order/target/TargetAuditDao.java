package com.guohuai.asset.manage.boot.duration.order.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TargetAuditDao extends JpaRepository<TargetAuditEntity, String>, JpaSpecificationExecutor<TargetAuditEntity> {

}
