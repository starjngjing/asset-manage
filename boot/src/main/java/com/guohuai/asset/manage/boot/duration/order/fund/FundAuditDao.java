package com.guohuai.asset.manage.boot.duration.order.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundAuditDao extends JpaRepository<FundAuditEntity, String>, JpaSpecificationExecutor<FundAuditEntity> {

}
