package com.guohuai.asset.manage.boot.acct.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountDao extends JpaRepository<Account, String> {

	@Query("from Account a order by a.oid asc")
	public List<Account> search();

}
