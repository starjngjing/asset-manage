package com.guohuai.asset.manage.boot.acct.books.document;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.acct.account.AccountService;
import com.guohuai.asset.manage.boot.acct.books.AccountBook;
import com.guohuai.asset.manage.boot.acct.books.AccountBookService;
import com.guohuai.asset.manage.boot.acct.books.document.entry.DocumentEntry;
import com.guohuai.asset.manage.boot.acct.books.document.entry.DocumentEntryService;
import com.guohuai.asset.manage.boot.acct.books.document.sn.DocumentSnService;
import com.guohuai.asset.manage.boot.acct.doc.template.DocTemplate;
import com.guohuai.asset.manage.boot.acct.doc.template.DocTemplateService;
import com.guohuai.asset.manage.boot.acct.doc.template.entry.DocTemplateEntry;
import com.guohuai.asset.manage.boot.acct.doc.template.entry.DocTemplateEntryService;
import com.guohuai.asset.manage.boot.acct.doc.word.DocWordService;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 凭证业务 - 资产池调仓
 * 
 * @author Arthur
 */
@Service
public class WarehouseDocumentService {

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
	 * 现金管理工具 申购
	 */
	@Transactional
	public Document cashtoolPurchase(String relative, String ticket, BigDecimal amount) {
		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0001_HBJJSG");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_CASHTOOL_WAREHOUSE);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_110151 = this.accountBookService.safeGet(relative, this.accountService.get("110151"));
		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));

		// 借 交易性金融资产_货币基金
		DocumentEntry e1_110151 = new DocumentEntry();
		e1_110151.setOid(StringUtil.uuid());
		e1_110151.setRelative(relative);
		e1_110151.setTicket(ticket);
		e1_110151.setDocument(document);
		e1_110151.setBook(b_110151);
		e1_110151.setDigest(null != maps && maps.containsKey("0030_0001_110151_01") ? maps.get("0030_0001_110151_01").getDigest() : "");
		e1_110151.setDrAmount(amount);
		e1_110151.setCrAmount(BigDecimal.ZERO);
		e1_110151.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(amount));
		b_110151 = this.accountBookService.drCredit(b_110151, amount);
		seq++;

		// 贷 银行存款
		DocumentEntry e2_1001 = new DocumentEntry();
		e2_1001.setOid(StringUtil.uuid());
		e2_1001.setRelative(relative);
		e2_1001.setTicket(ticket);
		e2_1001.setDocument(document);
		e2_1001.setBook(b_1001);
		e2_1001.setDigest(null != maps && maps.containsKey("0030_0001_1001_02") ? maps.get("0030_0001_1001_02").getDigest() : "");
		e2_1001.setDrAmount(BigDecimal.ZERO);
		e2_1001.setCrAmount(amount);
		e2_1001.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(amount));
		b_1001 = this.accountBookService.crCredit(b_1001, amount);
		seq++;

		document = this.documentDao.save(document);
		this.documentEntryService.save(e1_110151);
		this.documentEntryService.save(e2_1001);

		return document;

	}

	/**
	 * 现金管理工具 赎回
	 */
	@Transactional
	public Document cashtoolRedemption(String relative, String ticket, BigDecimal amount) {

		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0002_HBJJSH");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_CASHTOOL_WAREHOUSE);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));
		AccountBook b_110151 = this.accountBookService.safeGet(relative, this.accountService.get("110151"));

		// 借 银行存款
		DocumentEntry e1_1001 = new DocumentEntry();
		e1_1001.setOid(StringUtil.uuid());
		e1_1001.setRelative(relative);
		e1_1001.setTicket(ticket);
		e1_1001.setDocument(document);
		e1_1001.setBook(b_1001);
		e1_1001.setDigest(null != maps && maps.containsKey("0030_0002_1001_01") ? maps.get("0030_0002_1001_01").getDigest() : "");
		e1_1001.setDrAmount(amount);
		e1_1001.setCrAmount(BigDecimal.ZERO);
		e1_1001.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(amount));
		b_1001 = this.accountBookService.drCredit(b_1001, amount);
		seq++;

		// 贷 交易性金融资产_货币基金
		DocumentEntry e2_110151 = new DocumentEntry();
		e2_110151.setOid(StringUtil.uuid());
		e2_110151.setRelative(relative);
		e2_110151.setTicket(ticket);
		e2_110151.setDocument(document);
		e2_110151.setBook(b_110151);
		e2_110151.setDigest(null != maps && maps.containsKey("0030_0002_110151_02") ? maps.get("0030_0002_110151_02").getDigest() : "");
		e2_110151.setDrAmount(BigDecimal.ZERO);
		e2_110151.setCrAmount(amount);
		e2_110151.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(amount));
		b_110151 = this.accountBookService.crCredit(b_110151, amount);
		seq++;

		document = this.documentDao.save(document);
		this.documentEntryService.save(e1_1001);
		this.documentEntryService.save(e2_110151);

		return document;

	}

	/**
	 * 信托计划 申购
	 */
	@Transactional
	public Document targetPurchase(String relative, String ticket, BigDecimal amount) {

		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0003_XTZCSG");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_TARGET_PURCHASE);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_110101 = this.accountBookService.safeGet(relative, this.accountService.get("110101"));
		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));

		// 借 交易性金融资产_类别X
		DocumentEntry e1_110101 = new DocumentEntry();
		e1_110101.setOid(StringUtil.uuid());
		e1_110101.setRelative(relative);
		e1_110101.setTicket(ticket);
		e1_110101.setDocument(document);
		e1_110101.setBook(b_110101);
		e1_110101.setDigest(null != maps && maps.containsKey("0030_0003_110101_01") ? maps.get("0030_0003_110101_01").getDigest() : "");
		e1_110101.setDrAmount(amount);
		e1_110101.setCrAmount(BigDecimal.ZERO);
		e1_110101.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(amount));
		b_110101 = this.accountBookService.drCredit(b_110101, amount);
		seq++;

		// 贷 银行存款
		DocumentEntry e2_1001 = new DocumentEntry();
		e2_1001.setOid(StringUtil.uuid());
		e2_1001.setRelative(relative);
		e2_1001.setTicket(ticket);
		e2_1001.setDocument(document);
		e2_1001.setBook(b_1001);
		e2_1001.setDigest(null != maps && maps.containsKey("0030_0003_1001_02") ? maps.get("0030_0003_1001_02").getDigest() : "");
		e2_1001.setDrAmount(BigDecimal.ZERO);
		e2_1001.setCrAmount(amount);
		e2_1001.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(amount));
		b_1001 = this.accountBookService.crCredit(b_1001, amount);
		seq++;

		document = this.documentDao.save(document);
		this.documentEntryService.save(e1_110101);
		this.documentEntryService.save(e2_1001);

		return document;

	}

	/**
	 * 信托计划 本息兑付
	 */
	public void targetRedemption() {
		// 要判断是否有本金返还
	}

	/**
	 * 信托计划 溢价转让
	 * principal 本金
	 * estimate 估值
	 * revenue 交收额
	 */
	@Transactional
	public Document targetPremiumsTransOut(String relative, String ticket, BigDecimal principal, BigDecimal estimate, BigDecimal revenue) {

		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0005_XTZCYJZR");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_TARGET_TRANSFER);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_110101 = this.accountBookService.safeGet(relative, this.accountService.get("110101"));
		AccountBook b_2201 = this.accountBookService.safeGet(relative, this.accountService.get("2201"));
		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));
		AccountBook b_1201 = this.accountBookService.safeGet(relative, this.accountService.get("1201"));

		List<DocumentEntry> entries = new ArrayList<DocumentEntry>();

		// 有溢价, 调整未分配收益
		BigDecimal premiums = revenue.subtract(estimate);
		if (premiums.compareTo(BigDecimal.ZERO) > 0) {

			// 应收投资收益仍有头寸, 进行勾销冲账
			BigDecimal wipe = BigDecimal.ZERO;
			if (b_1201.getBalance().compareTo(BigDecimal.ZERO) > 0) {
				wipe = b_1201.getBalance().compareTo(premiums) > 0 ? premiums : b_1201.getBalance();
			}
			BigDecimal income = premiums.subtract(wipe);

			// 勾销之后仍有收益, 记未分配收益
			if (income.compareTo(BigDecimal.ZERO) > 0) {
				// 借 交易性金融资产_类别X
				DocumentEntry e1_110101 = new DocumentEntry();
				e1_110101.setOid(StringUtil.uuid());
				e1_110101.setRelative(relative);
				e1_110101.setTicket(ticket);
				e1_110101.setDocument(document);
				e1_110101.setBook(b_110101);
				e1_110101.setDigest(null != maps && maps.containsKey("0030_0005_110101_01") ? maps.get("0030_0005_110101_01").getDigest() : "");
				e1_110101.setDrAmount(income);
				e1_110101.setCrAmount(BigDecimal.ZERO);
				e1_110101.setSeq(seq);
				document.setDrAmount(document.getDrAmount().add(income));
				b_110101 = this.accountBookService.drCredit(b_110101, income);
				seq++;
				entries.add(e1_110101);

				// 贷 未分配收益
				DocumentEntry e2_2201 = new DocumentEntry();
				e2_2201.setOid(StringUtil.uuid());
				e2_2201.setRelative(relative);
				e2_2201.setTicket(ticket);
				e2_2201.setDocument(document);
				e2_2201.setBook(b_2201);
				e2_2201.setDigest(null != maps && maps.containsKey("0030_0005_2201_02") ? maps.get("0030_0005_2201_02").getDigest() : "");
				e2_2201.setDrAmount(BigDecimal.ZERO);
				e2_2201.setCrAmount(income);
				e2_2201.setSeq(seq);
				document.setCrAmount(document.getCrAmount().add(income));
				b_2201 = this.accountBookService.drCredit(b_2201, income);
				seq++;
				entries.add(e2_2201);
			}

			// 需要勾销应收投资收益头寸
			if (wipe.compareTo(BigDecimal.ZERO) > 0) {
				// 借 交易性金融资产_类别X
				DocumentEntry e3_110101 = new DocumentEntry();
				e3_110101.setOid(StringUtil.uuid());
				e3_110101.setRelative(relative);
				e3_110101.setTicket(ticket);
				e3_110101.setDocument(document);
				e3_110101.setBook(b_110101);
				e3_110101.setDigest(null != maps && maps.containsKey("0030_0005_110101_03") ? maps.get("0030_0005_110101_03").getDigest() : "");
				e3_110101.setDrAmount(wipe);
				e3_110101.setCrAmount(BigDecimal.ZERO);
				e3_110101.setSeq(seq);
				document.setDrAmount(document.getDrAmount().add(wipe));
				b_110101 = this.accountBookService.drCredit(b_110101, wipe);
				seq++;
				entries.add(e3_110101);

				// 贷 应收投资收益
				DocumentEntry e4_1201 = new DocumentEntry();
				e4_1201.setOid(StringUtil.uuid());
				e4_1201.setRelative(relative);
				e4_1201.setTicket(ticket);
				e4_1201.setDocument(document);
				e4_1201.setBook(b_1201);
				e4_1201.setDigest(null != maps && maps.containsKey("0030_0005_1201_04") ? maps.get("0030_0005_1201_04").getDigest() : "");
				e4_1201.setDrAmount(BigDecimal.ZERO);
				e4_1201.setCrAmount(wipe);
				e4_1201.setSeq(seq);
				document.setCrAmount(document.getCrAmount().add(wipe));
				b_1201 = this.accountBookService.crCredit(b_1201, wipe);
				seq++;
				entries.add(e4_1201);
			}
		}

		// 借 银行存款
		DocumentEntry e5_1001 = new DocumentEntry();
		e5_1001.setOid(StringUtil.uuid());
		e5_1001.setRelative(relative);
		e5_1001.setTicket(ticket);
		e5_1001.setDocument(document);
		e5_1001.setBook(b_1001);
		e5_1001.setDigest(null != maps && maps.containsKey("0030_0005_1001_05") ? maps.get("0030_0005_1001_05").getDigest() : "");
		e5_1001.setDrAmount(revenue);
		e5_1001.setCrAmount(BigDecimal.ZERO);
		e5_1001.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(revenue));
		b_1001 = this.accountBookService.drCredit(b_1001, revenue);
		seq++;
		entries.add(e5_1001);

		// 贷 交易性金融资产_类别X
		DocumentEntry e6_110101 = new DocumentEntry();
		e6_110101.setOid(StringUtil.uuid());
		e6_110101.setRelative(relative);
		e6_110101.setTicket(ticket);
		e6_110101.setDocument(document);
		e6_110101.setBook(b_110101);
		e6_110101.setDigest(null != maps && maps.containsKey("0030_0005_110101_06") ? maps.get("0030_0005_110101_06").getDigest() : "");
		e6_110101.setDrAmount(BigDecimal.ZERO);
		e6_110101.setCrAmount(revenue);
		e6_110101.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(revenue));
		b_110101 = this.accountBookService.crCredit(b_110101, revenue);
		seq++;
		entries.add(e6_110101);

		document = this.documentDao.save(document);
		for (DocumentEntry e : entries) {
			this.documentEntryService.save(e);
		}

		return document;
	}

	/**
	 * 信托计划 折价转让
	 * principal 本金
	 * estimate 估值
	 * revenue 交收额
	 */
	@Transactional
	public Document targetDiscountsTransOut(String relative, String ticket, BigDecimal principal, BigDecimal estimate, BigDecimal revenue) {

		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0006_XTZCZJZR");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_TARGET_TRANSFER);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_110101 = this.accountBookService.safeGet(relative, this.accountService.get("110101"));
		AccountBook b_2201 = this.accountBookService.safeGet(relative, this.accountService.get("2201"));
		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));
		AccountBook b_1201 = this.accountBookService.safeGet(relative, this.accountService.get("1201"));

		List<DocumentEntry> entries = new ArrayList<DocumentEntry>();

		BigDecimal discounts = estimate.subtract(revenue);

		BigDecimal wipe = BigDecimal.ZERO;

		if (discounts.compareTo(b_2201.getBalance()) > 0) {
			wipe = discounts.subtract(b_2201.getBalance());
		}

		BigDecimal income = discounts.subtract(wipe);

		// 勾销之后仍有收益, 记未分配收益
		if (income.compareTo(BigDecimal.ZERO) > 0) {
			// 借 未分配收益
			DocumentEntry e1_2201 = new DocumentEntry();
			e1_2201.setOid(StringUtil.uuid());
			e1_2201.setRelative(relative);
			e1_2201.setTicket(ticket);
			e1_2201.setDocument(document);
			e1_2201.setBook(b_2201);
			e1_2201.setDigest(null != maps && maps.containsKey("0030_0006_2201_01") ? maps.get("0030_0006_2201_01").getDigest() : "");
			e1_2201.setDrAmount(income);
			e1_2201.setCrAmount(BigDecimal.ZERO);
			e1_2201.setSeq(seq);
			document.setDrAmount(document.getDrAmount().add(income));
			b_2201 = this.accountBookService.crCredit(b_2201, income);
			seq++;
			entries.add(e1_2201);

			// 贷 交易性金融资产_类别X
			DocumentEntry e2_110101 = new DocumentEntry();
			e2_110101.setOid(StringUtil.uuid());
			e2_110101.setRelative(relative);
			e2_110101.setTicket(ticket);
			e2_110101.setDocument(document);
			e2_110101.setBook(b_110101);
			e2_110101.setDigest(null != maps && maps.containsKey("0030_0006_110101_02") ? maps.get("0030_0006_110101_02").getDigest() : "");
			e2_110101.setDrAmount(BigDecimal.ZERO);
			e2_110101.setCrAmount(income);
			e2_110101.setSeq(seq);
			document.setCrAmount(document.getCrAmount().add(income));
			b_110101 = this.accountBookService.crCredit(b_110101, income);
			seq++;
			entries.add(e2_110101);
		}

		// 需要调整应收投资收益头寸
		if (wipe.compareTo(BigDecimal.ZERO) > 0) {
			// 借 应收投资收益
			DocumentEntry e3_1201 = new DocumentEntry();
			e3_1201.setOid(StringUtil.uuid());
			e3_1201.setRelative(relative);
			e3_1201.setTicket(ticket);
			e3_1201.setDocument(document);
			e3_1201.setBook(b_1201);
			e3_1201.setDigest(null != maps && maps.containsKey("0030_0006_1201_03") ? maps.get("0030_0006_1201_03").getDigest() : "");
			e3_1201.setDrAmount(wipe);
			e3_1201.setCrAmount(BigDecimal.ZERO);
			e3_1201.setSeq(seq);
			document.setDrAmount(document.getDrAmount().add(wipe));
			b_1201 = this.accountBookService.drCredit(b_1201, wipe);
			seq++;
			entries.add(e3_1201);

			// 贷 交易性金融资产_类别X
			DocumentEntry e4_110101 = new DocumentEntry();
			e4_110101.setOid(StringUtil.uuid());
			e4_110101.setRelative(relative);
			e4_110101.setTicket(ticket);
			e4_110101.setDocument(document);
			e4_110101.setBook(b_110101);
			e4_110101.setDigest(null != maps && maps.containsKey("0030_0006_110101_04") ? maps.get("0030_0006_110101_04").getDigest() : "");
			e4_110101.setDrAmount(BigDecimal.ZERO);
			e4_110101.setCrAmount(wipe);
			e4_110101.setSeq(seq);
			document.setCrAmount(document.getCrAmount().add(wipe));
			b_110101 = this.accountBookService.crCredit(b_110101, wipe);
			seq++;
			entries.add(e4_110101);
		}

		// 借 银行存款
		DocumentEntry e5_1001 = new DocumentEntry();
		e5_1001.setOid(StringUtil.uuid());
		e5_1001.setRelative(relative);
		e5_1001.setTicket(ticket);
		e5_1001.setDocument(document);
		e5_1001.setBook(b_1001);
		e5_1001.setDigest(null != maps && maps.containsKey("0030_0006_1001_05") ? maps.get("0030_0006_1001_05").getDigest() : "");
		e5_1001.setDrAmount(revenue);
		e5_1001.setCrAmount(BigDecimal.ZERO);
		e5_1001.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(revenue));
		b_1001 = this.accountBookService.drCredit(b_1001, revenue);
		seq++;
		entries.add(e5_1001);

		// 贷 交易性金融资产_类别X
		DocumentEntry e6_110101 = new DocumentEntry();
		e6_110101.setOid(StringUtil.uuid());
		e6_110101.setRelative(relative);
		e6_110101.setTicket(ticket);
		e6_110101.setDocument(document);
		e6_110101.setBook(b_110101);
		e6_110101.setDigest(null != maps && maps.containsKey("0030_0006_110101_06") ? maps.get("0030_0006_110101_06").getDigest() : "");
		e6_110101.setDrAmount(BigDecimal.ZERO);
		e6_110101.setCrAmount(revenue);
		e6_110101.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(revenue));
		b_110101 = this.accountBookService.crCredit(b_110101, revenue);
		seq++;
		entries.add(e6_110101);

		document = this.documentDao.save(document);
		for (DocumentEntry e : entries) {
			this.documentEntryService.save(e);
		}

		return document;
	}

	/**
	 * 信托计划 转入
	 */
	public Document targetTransIn(String relative, String ticket, BigDecimal amount) {

		DocTemplate template = docTemplateService.safeGet("0030_NBTC_0007_XTZCZR");
		Map<String, DocTemplateEntry> maps = null;
		if (null != template) {
			maps = this.docTemplateEntryService.entryMap(template);
		}
		int seq = 1;
		long timestamp = System.currentTimeMillis();
		Date date = new Date(timestamp);
		Timestamp now = new Timestamp(timestamp);

		Document document = new Document();
		document.setOid(StringUtil.uuid());
		document.setDocword(null == template || null == template.getDocword() ? this.docWordService.get() : template.getDocword());
		document.setType(Document.TYPE_TARGET_PURCHASE);
		document.setRelative(relative);
		document.setTicket(ticket);
		document.setAcctSn(this.documentSnService.genSn(date));
		document.setAcctDate(date);
		document.setInvoiceNum(0);
		document.setDrAmount(BigDecimal.ZERO);
		document.setCrAmount(BigDecimal.ZERO);
		document.setCreateTime(now);
		document.setUpdateTime(now);

		AccountBook b_110101 = this.accountBookService.safeGet(relative, this.accountService.get("110101"));
		AccountBook b_1001 = this.accountBookService.safeGet(relative, this.accountService.get("1001"));

		// 借 交易性金融资产_类别X
		DocumentEntry e1_110101 = new DocumentEntry();
		e1_110101.setOid(StringUtil.uuid());
		e1_110101.setRelative(relative);
		e1_110101.setTicket(ticket);
		e1_110101.setDocument(document);
		e1_110101.setBook(b_110101);
		e1_110101.setDigest(null != maps && maps.containsKey("0030_0007_110101_01") ? maps.get("0030_0007_110101_01").getDigest() : "");
		e1_110101.setDrAmount(amount);
		e1_110101.setCrAmount(BigDecimal.ZERO);
		e1_110101.setSeq(seq);
		document.setDrAmount(document.getDrAmount().add(amount));
		b_110101 = this.accountBookService.drCredit(b_110101, amount);
		seq++;

		// 贷 银行存款
		DocumentEntry e2_1001 = new DocumentEntry();
		e2_1001.setOid(StringUtil.uuid());
		e2_1001.setRelative(relative);
		e2_1001.setTicket(ticket);
		e2_1001.setDocument(document);
		e2_1001.setBook(b_1001);
		e2_1001.setDigest(null != maps && maps.containsKey("0030_0007_1001_02") ? maps.get("0030_0007_1001_02").getDigest() : "");
		e2_1001.setDrAmount(BigDecimal.ZERO);
		e2_1001.setCrAmount(amount);
		e2_1001.setSeq(seq);
		document.setCrAmount(document.getCrAmount().add(amount));
		b_1001 = this.accountBookService.crCredit(b_1001, amount);
		seq++;

		document = this.documentDao.save(document);
		this.documentEntryService.save(e1_110101);
		this.documentEntryService.save(e2_1001);

		return document;

	}

}
