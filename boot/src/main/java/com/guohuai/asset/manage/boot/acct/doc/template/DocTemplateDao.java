package com.guohuai.asset.manage.boot.acct.doc.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocTemplateDao extends JpaRepository<DocTemplate, String>, JpaSpecificationExecutor<DocTemplate> {

}
