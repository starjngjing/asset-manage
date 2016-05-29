package com.guohuai.asset.manage.boot.acct.books;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guohuai.asset.manage.boot.acct.account.Account;

public interface AccountBookDao extends JpaRepository<AccountBook, String> {

	public List<AccountBook> findByRelative(String relative);

	public AccountBook findByRelativeAndAccount(String relative, Account account);

	@Query("from AccountBook b order by b.account.oid asc")
	public List<AccountBook> search();

}
