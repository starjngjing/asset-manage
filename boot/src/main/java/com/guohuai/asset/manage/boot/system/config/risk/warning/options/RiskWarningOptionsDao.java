package com.guohuai.asset.manage.boot.system.config.risk.warning.options;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskWarningOptionsDao extends JpaRepository<RiskWarningOptions, String>, JpaSpecificationExecutor<RiskWarningOptions> {

}
