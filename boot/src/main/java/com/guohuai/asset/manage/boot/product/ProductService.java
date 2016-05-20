package com.guohuai.asset.manage.boot.product;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.dict.Dict;
import com.guohuai.asset.manage.boot.dict.DictService;
import com.guohuai.asset.manage.boot.file.File;
import com.guohuai.asset.manage.boot.file.FileResp;
import com.guohuai.asset.manage.boot.file.FileService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;

@Service
@Transactional
public class ProductService {
	
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductLogDao productLogDao;
	@Autowired
	private DictService dictService;
	@Autowired
	private FileService fileService;

	@Transactional
	public BaseResp savePeriodic(SavePeriodicProductForm form, String operator) throws ParseException {

		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());

		Product.ProductBuilder pb = Product.builder().oid(StringUtil.uuid());
		{
			pb.code(form.getCode()).name(form.getName()).fullName(form.getFullName()).administrator(form.getAdministrator()).status(Product.STATE_Create).isDeleted(Product.NO);
		}
		{
			// 产品类型 
			Dict assetType = this.dictService.get(form.getTypeOid());
			pb.type(assetType);
		}
		{
			pb.reveal(form.getReveal()).currency(form.getCurrency()).incomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.manageRate(new BigDecimal(form.getFixedManageRate()));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.fixedManageRate(new BigDecimal(form.getFixedManageRate()));
			}
		}
		{
			//募集开始时间类型;募集期:()个自然日;起息日:募集满额后()个自然日 存续期数;存续期单位;存续期:()个自然日
			pb.raiseStartDateType(form.getRaiseStartDateType()).raisePeriod(form.getRaisePeriod()).interestsFirstDate(form.getInterestsFirstDate()).durationPeriod(form.getDurationPeriod());
		}
		{
			//募集开始时间  募集期:()个自然日  起息日:募集满额后()个自然日  存续期:()个自然日
			if(form.getRaiseStatrtDate()!=null) {
				Date raiseStartDate = DateUtil.parseDate(form.getRaiseStatrtDate(), DateUtil.datetimePattern);
				pb.raiseStartDate(new Timestamp(raiseStartDate.getTime()));
				pb.raiseEndDate(new Timestamp(DateUtil.addDay(raiseStartDate, form.getRaisePeriod()).getTime()));//募集结束时间
				pb.setupDate(new Timestamp(DateUtil.addDay(raiseStartDate, form.getRaisePeriod()+form.getInterestsFirstDate()).getTime()));//产品成立时间（存续期开始时间）
				pb.durationPeriodEndDate(new Timestamp(DateUtil.addDay(raiseStartDate, form.getRaisePeriod()+form.getInterestsFirstDate()+form.getDurationPeriod()).getTime()));//存续期结束时间
				//到期最晚还本付息日 指存续期结束后的还本付息最迟发生在存续期后的第X个自然日的23:59:59为止
				Date repayDate = DateUtil.addDay(raiseStartDate, form.getRaisePeriod()+form.getInterestsFirstDate()+form.getDurationPeriod()+form.getAccrualDate());
				String repayDateStr = DateUtil.format(repayDate, DateUtil.datePattern);
				pb.accrualLastDate(new Timestamp(DateUtil.parseDate(repayDateStr+" 23:59:59", DateUtil.datetimePattern).getTime()));//到期还款时间
			}
		}
		{
			// 年化收益
			pb.expAror(new BigDecimal(form.getExpAror()));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				pb.expArorSec(new BigDecimal(form.getExpAror()));
			} else {
				pb.expArorSec(new BigDecimal(form.getExpArorSec()));
			}
		}
		{
			pb.raisedTotalNumber(form.getRaisedTotalNumber()).investMin(form.getInvestMin()).investMax(form.getInvestMax()).purchaseLimit(form.getPurchaseLimit()).investAdditional(form.getInvestAdditional()).netUnitShare(new BigDecimal(form.getNetUnitShare())).accrualDate(form.getAccrualDate());
		}
		{
			pb.investComment(form.getInvestComment()).instruction(form.getInstruction()).riskLevel(form.getRiskLevel()).stems(Product.STEMS_Userdefine).auditState(Product.AUDIT_STATE_Nocommit);
		}
		{
			// 其他字段 初始化默认值s
			pb.operator(operator).publisher(operator).updateTime(now).createTime(now);
		}
		String fkey = StringUtil.uuid();
		{
			if (null != form.getFiles() && form.getFiles().size() > 0) {
				pb.fileKeys(fkey);
			} else {
				pb.fileKeys(StringUtil.EMPTY);
			}
		}
		
		Product p = pb.build();
		p = this.productDao.save(p);
		if (null != form.getFiles() && form.getFiles().size() > 0) {
			// 文件
			this.fileService.save(form.getFiles(), fkey, File.CATE_User, operator);
		}
		return response;
	}
	
	@Transactional
	public BaseResp saveCurrent(SaveCurrentProductForm form, String operator) throws ParseException {

		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());

		Product.ProductBuilder pb = Product.builder().oid(StringUtil.uuid());
		{
			pb.code(form.getCode()).name(form.getName()).fullName(form.getFullName()).administrator(form.getAdministrator()).status(Product.STATE_Create).isDeleted(Product.NO);
		}
		{
			// 产品类型 
			Dict type = this.dictService.get(form.getTypeOid());
			pb.type(type);
			// 收益结转周期
			pb.accrualCycleOid(form.getAccrualCycleOid());
			// 付利方式
			pb.payModeOid(form.getPayModeOid());
		}
		{
			pb.payModeDate(form.getPayModeDate());
		}
		{
			pb.reveal(form.getReveal()).currency(form.getCurrency()).incomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.manageRate(new BigDecimal(form.getFixedManageRate()));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.fixedManageRate(new BigDecimal(form.getFixedManageRate()));
			}
		}
		{
			//产品成立时间类型;起息日;锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
			//申购确认日:()个;申购确认日类型:自然日或交易日
			//赎回确认日:()个;赎回确认日类型:自然日或交易日
			pb.setupDateType(form.getSetupDateType()).interestsFirstDate(form.getInterestsFirstDate()).lockPeriod(form.getLockPeriod())
			.purchaseConfirmDate(form.getPurchaseConfirmDate()).purchaseConfirmDateType(form.getPurchaseConfirmDateType()).
			redeemConfirmDate(form.getRedeemConfirmDate()).redeemConfirmDateType(form.getRedeemConfirmDateType())
			.redeemTimingTaskDateType(form.getRedeemTimingTaskDateType()).redeemTimingTaskTime(Time.valueOf(form.getRedeemTimingTaskTime()))
			.redeemTimingTaskDate(1);//redeemTimingTaskDate 默认每日
			//产品成立时间（存续期开始时间）
			if(form.getSetupDate()!=null) {
				Date setupDate = DateUtil.parseDate(form.getSetupDate(), DateUtil.datetimePattern);
				pb.setupDate(new Timestamp(setupDate.getTime()));
			}
		}
		{
			// 年化收益
			pb.expAror(new BigDecimal(form.getExpAror()));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				pb.expArorSec(new BigDecimal(form.getExpAror()));
			} else {
				pb.expArorSec(new BigDecimal(form.getExpArorSec()));
			}
		}
		{
			pb.investMin(form.getInvestMin()).investMax(form.getInvestMax()).purchaseLimit(form.getPurchaseLimit()).investAdditional(form.getInvestAdditional()).netUnitShare(new BigDecimal(form.getNetUnitShare())).netMaxRredeemDay(form.getNetMaxRredeemDay());
		}
		{
			pb.isOpenPurchase(Product.NO).isOpenRemeed(Product.NO);
		}
		{
			pb.investComment(form.getInvestComment()).instruction(form.getInstruction()).riskLevel(form.getRiskLevel()).stems(Product.STEMS_Userdefine).auditState(Product.AUDIT_STATE_Nocommit);
		}
		{
			// 其他字段 初始化默认值s
			pb.operator(operator).publisher(operator).updateTime(now).createTime(now);
		}
		String fkey = StringUtil.uuid();
		{
			if (null != form.getFiles() && form.getFiles().size() > 0) {
				pb.fileKeys(fkey);
			} else {
				pb.fileKeys(StringUtil.EMPTY);
			}
		}
		
		Product p = pb.build();
		p = this.productDao.save(p);
		if (null != form.getFiles() && form.getFiles().size() > 0) {
			// 文件
			this.fileService.save(form.getFiles(), fkey, File.CATE_User, operator);
		}
		
		return response;
	}
	
	/**
	 * 获取指定id的产品对象
	 * 
	 * @param pid
	 *            产品对象id
	 * @return {@link Product}
	 */
	public Product getProductById(String pid) {
		Product product = this.productDao.findOne(pid);
		if (product == null || Product.YES.equals(product.getIsDeleted())) {
			throw AMPException.getException(90000);
		}
		return product;
		
	}
	
	@Transactional
	public Product delete(String oid, String operator) {
		Product product = this.getProductById(oid);
		
//		//面向渠道的属性，同一产品在不同渠道可以为不同状态，同一产品，只要有一个渠道销售状态为“渠道X待上架”，该产品已录入详情字段就不可修改。
//		List<ProductChannel> pcs = this.productChannelDao.findProductChannelByProductOid(product.getOid());
//		if(pcs!=null && pcs.size()>0) {
//			for(ProductChannel pc : pcs) {
//				if(ProductChannel.MARKET_STATE_Shelfing.equals(pc.getMarketState()) 
//						|| ProductChannel.MARKET_STATE_Onshelf.equals(pc.getMarketState())) {
//					throw AMPException.getException(30012);
//				}
//			}
//		}
		
		{
			product.setIsDeleted(Product.YES);
			// 其它：修改时间、操作人
			product.setOperator(operator);
			product.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		}
		product = this.productDao.saveAndFlush(product);
		//删除渠道和商品关联关系
//		productChannelDao.deleteByProductOid(oid);
					
		return product;
	}
	
	/**
	 * 进行更新产品信息的操作，同时记录并存储日志
	 * 
	 * @param form
	 * @param operator
	 * @return
	 */
	@Transactional
	public BaseResp updatePeriodic(SavePeriodicProductForm form, String operator) throws ParseException {

		BaseResp response = new BaseResp();
		
		// 根据form中的productOid，从数据库得到相应对象，之后进行为对象进行审核操作
		Product product = this.getProductById(form.getOid());
		// 当前时间
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		//未提交审核的可修改
		if(!Product.AUDIT_STATE_Nocommit.equals(product.getAuditState())) {
			throw AMPException.getException(90008);
		}
		// 判断是否可以修改 名称类型不变
		{
			product.setCode(form.getCode());
			product.setName(form.getName());
			product.setFullName(form.getFullName());
			product.setAdministrator(form.getAdministrator());
			product.setStatus(Product.STATE_Update);
			product.setReveal(form.getReveal());
			product.setCurrency(form.getCurrency());
			product.setIncomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setManageRate(new BigDecimal(form.getFixedManageRate()));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setFixedManageRate(new BigDecimal(form.getFixedManageRate()));
			}
		}
		{
			// 年化收益
			product.setExpAror(new BigDecimal(form.getExpAror()));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				product.setExpArorSec(new BigDecimal(form.getExpAror()));
			} else {
				product.setExpArorSec(new BigDecimal(form.getExpArorSec()));
			}
		}
		{
			//募集开始时间类型;募集期:()个自然日;起息日:募集满额后()个自然日;存续期:()个自然日
			product.setRaiseStartDateType(form.getRaiseStartDateType());
			product.setRaisePeriod(form.getRaisePeriod());
			product.setInterestsFirstDate(form.getInterestsFirstDate());
			product.setDurationPeriod(form.getDurationPeriod());
		}
		
		if(Product.DATE_TYPE_FirstRackTime.equals(form.getRaiseStartDateType())) {
			product.setRaiseStartDate(null);
		} else {
			Date raiseStatrtDate = DateUtil.parseDate(form.getRaiseStatrtDate(), DateUtil.datetimePattern);
			product.setRaiseStartDate(new Timestamp(raiseStatrtDate.getTime()));
			product.setRaiseEndDate(new Timestamp(DateUtil.addDay(raiseStatrtDate, form.getRaisePeriod()).getTime()));//募集结束时间
			product.setSetupDate(new Timestamp(DateUtil.addDay(raiseStatrtDate, form.getRaisePeriod()+form.getInterestsFirstDate()).getTime()));//产品成立时间（存续期开始时间）
			product.setDurationPeriodEndDate(new Timestamp(DateUtil.addDay(raiseStatrtDate, form.getRaisePeriod()+form.getInterestsFirstDate()+form.getDurationPeriod()).getTime()));//存续期结束时间
			//到期最晚还本付息日 指存续期结束后的还本付息最迟发生在存续期后的第X个自然日的23:59:59为止
			Date repayDate = DateUtil.addDay(raiseStatrtDate, form.getRaisePeriod()+form.getInterestsFirstDate()+form.getDurationPeriod()+form.getAccrualDate());
			String repayDateStr = DateUtil.format(repayDate, DateUtil.datePattern);
			product.setAccrualLastDate(new Timestamp(DateUtil.parseDate(repayDateStr+" 23:59:59", DateUtil.datetimePattern).getTime()));//到期还款时间
		}
		{
			product.setRaisedTotalNumber(form.getRaisedTotalNumber());
			product.setInvestMin(form.getInvestMin());
			product.setInvestMax(form.getInvestMax());
			product.setPurchaseLimit(form.getPurchaseLimit());
			product.setInvestAdditional(form.getInvestAdditional());
			product.setNetUnitShare(new BigDecimal(form.getNetUnitShare()));
			product.setAccrualDate(form.getAccrualDate());
		}
		{
			product.setInvestComment(form.getInvestComment());
			product.setInstruction(form.getInstruction());
			product.setRiskLevel(form.getRiskLevel());
		}
		String fkey = null;
		{
			if (StringUtil.isEmpty(product.getFileKeys())) {
				fkey = StringUtil.uuid();
				product.setFileKeys(fkey);
			} else {
				fkey = product.getFileKeys();
			}
		}
		product.setFileKeys(fkey);
		{
			// 其它：修改时间、操作人
			product.setOperator(operator);
			product.setUpdateTime(now);
		}
		// 更新产品
		product = this.productDao.saveAndFlush(product);

		{
			// 文件
			this.fileService.save(form.getFiles(), fkey, File.CATE_User, operator);
		}
		
		return response;
	}
	
	
	/**
	 * 进行更新产品信息的操作，同时记录并存储日志
	 * 
	 * @param form
	 * @param operator
	 * @return
	 */
	@Transactional
	public BaseResp updateCurrent(SaveCurrentProductForm form, String operator) throws ParseException {
		// 根据form中的productOid，从数据库得到相应对象，之后进行为对象进行审核操作
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(form.getOid());
		
		// 当前时间
		Timestamp now = new Timestamp(System.currentTimeMillis());
		//未提交审核的可修改
		if(!Product.AUDIT_STATE_Nocommit.equals(product.getAuditState())) {
			throw AMPException.getException(90008);
		}
		// 判断是否可以修改 名称类型不变
		{
			product.setCode(form.getCode());
			product.setName(form.getName());
			product.setFullName(form.getFullName());
			product.setAdministrator(form.getAdministrator());
			product.setStatus(Product.STATE_Update);
			product.setReveal(form.getReveal());
			product.setCurrency(form.getCurrency());
			product.setIncomeCalcBasis(form.getIncomeCalcBasis());
		}	
		
		{
			//收益结转周期
			product.setAccrualCycleOid(form.getAccrualCycleOid());
			// 付利方式
			product.setPayModeOid(form.getPayModeOid());
		}
		{
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setManageRate(new BigDecimal(form.getFixedManageRate()));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setFixedManageRate(new BigDecimal(form.getFixedManageRate()));
			}
		}
		{
			// 年化收益
			product.setExpAror(new BigDecimal(form.getExpAror()));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				product.setExpArorSec(new BigDecimal(form.getExpAror()));
			} else {
				product.setExpArorSec(new BigDecimal(form.getExpArorSec()));
			}
		}
		{
			product.setSetupDateType(form.getSetupDateType());
			product.setInterestsFirstDate(form.getInterestsFirstDate());
			product.setLockPeriod(form.getLockPeriod());
			product.setPurchaseConfirmDate(form.getPurchaseConfirmDate());
			product.setPurchaseConfirmDateType(form.getPurchaseConfirmDateType());
			product.setRedeemConfirmDate(form.getRedeemConfirmDate());
			product.setRedeemConfirmDateType(form.getRedeemConfirmDateType());
			product.setRedeemTimingTaskDateType(form.getRedeemTimingTaskDateType());
			product.setRedeemTimingTaskTime(Time.valueOf(form.getRedeemTimingTaskTime()));
			
			if(Product.DATE_TYPE_FirstRackTime.equals(form.getSetupDateType())) {
				form.setSetupDate(null);
			} else {
			//产品成立时间（存续期开始时间）
				Date setupDate = DateUtil.parseDate(form.getSetupDate(), DateUtil.datetimePattern);
				product.setSetupDate(new Timestamp(setupDate.getTime()));
			}
		}
		{
			product.setInvestMin(form.getInvestMin());
			product.setInvestMax(form.getInvestMax());
			product.setPurchaseLimit(form.getPurchaseLimit());
			product.setInvestAdditional(form.getInvestAdditional());
			product.setNetUnitShare(new BigDecimal(form.getNetUnitShare()));
			product.setNetMaxRredeemDay(form.getNetMaxRredeemDay());
		}
		{
			product.setInvestComment(form.getInvestComment());
			product.setInstruction(form.getInstruction());
			product.setRiskLevel(form.getRiskLevel());
		}
		{
			// 其它：修改时间、操作人
			product.setOperator(operator);
			product.setUpdateTime(now);
		}
		
		String fkey = null;
		{
			if (StringUtil.isEmpty(product.getFileKeys())) {
				fkey = StringUtil.uuid();
				product.setFileKeys(fkey);
			} else {
				fkey = product.getFileKeys();
			}
		}
		
		product.setFileKeys(fkey);
		// 更新产品
		product = this.productDao.saveAndFlush(product);

		{
			// 文件
			this.fileService.save(form.getFiles(), fkey, File.CATE_User, operator);
		}
		
		return response;
	}
	
	/**
	 * 产品详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public ProductDetailResp read(String oid) {
		Product product = this.getProductById(oid);
		ProductDetailResp pr = new ProductDetailResp(product);

		if (!StringUtil.isEmpty(product.getFileKeys())) {
			List<File> files = this.fileService.list(product.getFileKeys(), File.STATE_Valid);
			if (files.size() > 0) {
				pr.setFiles(new ArrayList<FileResp>());
				for (File file : files) {
					pr.getFiles().add(new FileResp(file));
				}
			}
		}

		return pr;
	}
	
	/**
	 * 查询
	 * @param spec
	 * @param pageable
	 * @return {@link PagesRep<ProductResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	public PageResp<ProductResp> list(Specification<Product> spec, Pageable pageable) {
		Page<Product> cas = this.productDao.findAll(spec, pageable);
		PageResp<ProductResp> pagesRep = new PageResp<ProductResp>();
		if (cas != null && cas.getContent() != null && cas.getTotalElements() > 0) {
			List<ProductResp> rows = new ArrayList<ProductResp>();
			for (Product p : cas) {
				ProductResp queryRep = new ProductResp(p);
				rows.add(queryRep);
			}
			pagesRep.setRows(rows);
		}
		pagesRep.setTotal(cas.getTotalElements());
		return pagesRep;
	}
	
	
	@Transactional
	public BaseResp aduitApply(List<String> oids, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		List<Product> ps = productDao.findByOidIn(oids);
		if (ps == null || ps.size()==0) {
			throw AMPException.getException(90000);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		for(Product product : ps) {
			if (product == null || Product.YES.equals(product.getIsDeleted())) {
				throw AMPException.getException(90000);
			}
			if (product.getAssetPool() == null) {
				throw AMPException.getException(90011);
			}
			product.setStatus(Product.STATE_Auditing);
			product.setAuditState(Product.AUDIT_STATE_Auditing);
			product.setOperator(operator);
			product.setUpdateTime(now);
			
			ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
			{
				plb.product(product).auditType(ProductLog.AUDIT_TYPE_Auditing).auditState(ProductLog.AUDIT_STATE_Commited).auditor(operator).auditTime(now);
			}
			
			this.productDao.saveAndFlush(product);
			this.productLogDao.save(plb.build());
			
		}
		
		return response;
	}
	
	@Transactional
	public BaseResp aduitApprove(String oid, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Auditpass);
		product.setAuditState(Product.AUDIT_STATE_Reviewing);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Auditing).auditState(ProductLog.AUDIT_STATE_Approval).auditor(operator).auditTime(now);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
	@Transactional
	public BaseResp aduitReject(String oid, String auditComment, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Auditfail);
		product.setAuditState(Product.AUDIT_STATE_Nocommit);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Auditing).auditState(ProductLog.AUDIT_STATE_Reject).auditor(operator).auditTime(now).auditComment(auditComment);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
	
	@Transactional
	public BaseResp reviewApprove(String oid, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Reviewpass);
		product.setAuditState(Product.AUDIT_STATE_Approvaling);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Reviewing).auditState(ProductLog.AUDIT_STATE_Approval).auditor(operator).auditTime(now);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
	@Transactional
	public BaseResp reviewReject(String oid, String auditComment, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Reviewfail);
		product.setAuditState(Product.AUDIT_STATE_Auditing);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Reviewing).auditState(ProductLog.AUDIT_STATE_Reject).auditor(operator).auditTime(now).auditComment(auditComment);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
	
	@Transactional
	public BaseResp admitApprove(String oid, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Admitpass);
		product.setAuditState(Product.AUDIT_STATE_Approval);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Approving).auditState(ProductLog.AUDIT_STATE_Approval).auditor(operator).auditTime(now);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
	@Transactional
	public BaseResp admitReject(String oid, String auditComment, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		Product product = this.getProductById(oid);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setStatus(Product.STATE_Admitfail);
		product.setAuditState(Product.AUDIT_STATE_Reviewing);
		product.setOperator(operator);
		product.setUpdateTime(now);
		
		ProductLog.ProductLogBuilder plb = ProductLog.builder().oid(StringUtil.uuid());
		{
			plb.product(product).auditType(ProductLog.AUDIT_TYPE_Approving).auditState(ProductLog.AUDIT_STATE_Reject).auditor(operator).auditTime(now).auditComment(auditComment);
		}
		
		this.productDao.saveAndFlush(product);
		this.productLogDao.save(plb.build());
		
		return response;
	}
	
}
