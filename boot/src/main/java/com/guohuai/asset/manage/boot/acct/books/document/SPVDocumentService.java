package com.guohuai.asset.manage.boot.acct.books.document;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.acct.account.AccountService;
import com.guohuai.asset.manage.boot.acct.books.AccountBookService;
import com.guohuai.asset.manage.boot.acct.books.document.entry.DocumentEntryService;
import com.guohuai.asset.manage.boot.acct.books.document.sn.DocumentSnService;
import com.guohuai.asset.manage.boot.acct.doc.template.DocTemplateService;
import com.guohuai.asset.manage.boot.acct.doc.template.entry.DocTemplateEntryService;
import com.guohuai.asset.manage.boot.acct.doc.word.DocWordService;
import com.guohuai.asset.manage.component.exception.AMPException;

/**
 * 
 * 凭证业务 - SPV 申购/赎回
 * 
 * @author Arthur
 *
 */
@Service
public class SPVDocumentService {

	@Autowired
	private DocumentDao documentDao;
	@Autowired
	private DocWordService docWordService;
	@Autowired
	private DocumentSnService documentSnService;
	@Autowired
	private DocumentEntryService documentEntryService;
	@Autowired
	private DocTemplateService docTemplateService;
	@Autowired
	private DocTemplateEntryService docTemplateEntryService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountBookService accountBookService;

	/**
	 * 
	 * @param relative
	 *            资产池oid
	 * @param ticket
	 *            订单oid
	 * @param market
	 *            申购确认市值
	 * @param saleable
	 *            可售份额
	 * @param chargefee
	 *            手续费/应付费金
	 * @return 会计凭证
	 */
	@Transactional
	public Document purchase(String relative, String ticket, BigDecimal market, BigDecimal saleable, BigDecimal chargefee) {

		// market = saleaable + chargefee;
		if (market.compareTo(saleable.add(chargefee)) != 0) {
			throw new AMPException("会计分录试算不平衡.");
		}

		// 若可售份额的增加 < SPV申购确认的市值增加
		if (saleable.compareTo(market) <= 0) {
			
		}

		// 若可售份额的增加 > SPV申购确认的市值增加
		if (saleable.compareTo(market) > 0) {

		}

		return null;
	}

}
