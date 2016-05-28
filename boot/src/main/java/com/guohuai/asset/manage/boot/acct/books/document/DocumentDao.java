package com.guohuai.asset.manage.boot.acct.books.document;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentDao extends JpaRepository<Document, String> {

	@Query(value = "SELECT IFNULL(MAX(acctSn), 0) + 1 FROM `T_ACCT_DOCUMENT` WHERE acctDate BETWEEN ?1 AND ?2", nativeQuery = true)
	public int maxSn(Date begin, Date end);

}
