package com.guohuai.asset.manage.boot.acct.books;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.acct.account.Account;
import com.guohuai.asset.manage.boot.acct.account.AccountService;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class AccountBookService {

	@Autowired
	private AccountBookDao accountBookDao;
	@Autowired
	private AccountService accountService;

	@Transactional
	public List<AccountBook> init(String relative) {

		List<Account> accounts = this.accountService.search();

		List<AccountBook> persisted = this.accountBookDao.findByRelative(relative);
		Map<String, AccountBook> exists = new HashMap<String, AccountBook>();
		if (null != persisted && persisted.size() > 0) {
			for (AccountBook book : persisted) {
				exists.put(book.getAccount().getOid(), book);
			}
		}

		List<AccountBook> books = new ArrayList<AccountBook>();

		for (Account account : accounts) {
			if (exists.containsKey(account.getOid())) {
				books.add(exists.get(account.getOid()));
			} else {
				AccountBook book = new AccountBook();
				book.setOid(StringUtil.uuid());
				book.setRelative(relative);
				book.setAccount(account);
				book.setBalance(BigDecimal.ZERO);
				book.setOpeningBalance(BigDecimal.ZERO);
				book = this.accountBookDao.save(book);
				books.add(book);
			}
		}

		return books;
	}

	@Transactional
	public AccountBook safeGet(String relative, Account account) {
		AccountBook book = this.accountBookDao.findByRelativeAndAccount(relative, account);
		if (null == book) {
			book = new AccountBook();
			book.setOid(StringUtil.uuid());
			book.setRelative(relative);
			book.setAccount(account);
			book.setBalance(BigDecimal.ZERO);
			book.setOpeningBalance(BigDecimal.ZERO);
			book = this.accountBookDao.save(book);
		}
		return book;
	}

	@Transactional
	public AccountBook drCredit(AccountBook book, BigDecimal cash) {
		book.setBalance(book.getBalance().add(cash));
		book = this.accountBookDao.save(book);

		if (!StringUtil.isEmpty(book.getAccount().getParent())) {
			AccountBook parent = this.safeGet(book.getRelative(), this.accountService.get(book.getAccount().getParent()));
			this.drCredit(parent, cash);
		}

		return book;
	}

	@Transactional
	public AccountBook crCredit(AccountBook book, BigDecimal cash) {
		book.setBalance(book.getBalance().subtract(cash));
		book = this.accountBookDao.save(book);

		if (!StringUtil.isEmpty(book.getAccount().getParent())) {
			AccountBook parent = this.safeGet(book.getRelative(), this.accountService.get(book.getAccount().getParent()));
			this.crCredit(parent, cash);
		}

		return book;
	}

}
