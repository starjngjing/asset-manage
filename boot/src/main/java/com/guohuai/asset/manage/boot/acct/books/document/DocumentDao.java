package com.guohuai.asset.manage.boot.acct.books.document;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DocumentDao extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {

}
