package com.guohuai.asset.manage.boot.duration.order.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TargetTransDao extends JpaRepository<TargetTransEntity, String>, JpaSpecificationExecutor<TargetTransEntity> {

	public TargetTransEntity findByOid(String oid);
}
