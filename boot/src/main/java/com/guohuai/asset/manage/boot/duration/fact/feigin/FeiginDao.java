package com.guohuai.asset.manage.boot.duration.fact.feigin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeiginDao extends JpaRepository<FeiginEntity, String>, JpaSpecificationExecutor<FeiginEntity> {

}
