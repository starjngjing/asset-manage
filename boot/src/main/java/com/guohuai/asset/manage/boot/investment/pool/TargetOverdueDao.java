package com.guohuai.asset.manage.boot.investment.pool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TargetOverdueDao
		extends JpaRepository<TargetOverdue, String>, JpaSpecificationExecutor<TargetOverdue> {

}
