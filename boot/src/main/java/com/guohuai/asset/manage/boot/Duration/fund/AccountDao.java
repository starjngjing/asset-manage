package com.guohuai.asset.manage.boot.Duration.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountDao extends JpaRepository<AccountEntity, String>, JpaSpecificationExecutor<AccountEntity> {

}
