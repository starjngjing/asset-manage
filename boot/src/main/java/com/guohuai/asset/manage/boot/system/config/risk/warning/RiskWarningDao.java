package com.guohuai.asset.manage.boot.system.config.risk.warning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskWarningDao extends JpaRepository<RiskWarning, String>, JpaSpecificationExecutor<RiskWarning> {

}
