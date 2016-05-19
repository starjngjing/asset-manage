package com.guohuai.asset.manage.boot.duration.capital;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CapitalDao extends JpaRepository<CapitalEntity, String>, JpaSpecificationExecutor<CapitalEntity> {

}
