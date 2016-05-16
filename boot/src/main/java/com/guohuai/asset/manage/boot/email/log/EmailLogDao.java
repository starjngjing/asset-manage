package com.guohuai.asset.manage.boot.email.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailLogDao extends JpaRepository<EmailLog, String>, JpaSpecificationExecutor<EmailLog> {

}
