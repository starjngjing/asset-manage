package com.guohuai.asset.manage.boot.duration.order.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundDao extends JpaRepository<FundEntity, String>, JpaSpecificationExecutor<FundEntity> {

}
