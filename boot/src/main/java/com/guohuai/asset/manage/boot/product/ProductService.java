package com.guohuai.asset.manage.boot.product;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.guohuai.asset.manage.boot.dict.Dict;
import com.guohuai.asset.manage.boot.dict.DictService;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolDao;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.file.File;
import com.guohuai.asset.manage.boot.file.FileResp;
import com.guohuai.asset.manage.boot.file.FileService;
import com.guohuai.asset.manage.boot.file.SaveFileForm;
import com.guohuai.asset.manage.boot.product.productChannel.ProductChannel;
import com.guohuai.asset.manage.boot.product.productChannel.ProductChannelService;
import com.guohuai.asset.manage.boot.product.reward.ProductIncomeReward;
import com.guohuai.asset.manage.boot.product.reward.ProductIncomeRewardService;
import com.guohuai.asset.manage.boot.product.reward.ProductRewardResp;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;

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
	@Autowired
	private ProductChannelService productChannelService;
	@Autowired
	private AdminSdk adminSdk;
	@Autowired
	private AssetPoolDao assetPoolDao;
	@Autowired
	private ProductIncomeRewardService productRewardService;

	@Transactional
	public BaseResp savePeriodic(SavePeriodicProductForm form, String operator) throws ParseException {

		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());

		Product.ProductBuilder pb = Product.builder().oid(StringUtil.uuid());
		{
			pb.code(form.getCode()).name(form.getName()).fullName(form.getFullName()).administrator(form.getAdministrator()).state(Product.STATE_Create).isDeleted(Product.NO);
		}
		{
			// 产品类型 
			Dict assetType = this.dictService.get(form.getTypeOid());
			pb.type(assetType);
		}
		if(!StringUtil.isEmpty(form.getAssetPoolOid())) {
			AssetPoolEntity assetPool = assetPoolDao.findOne(form.getAssetPoolOid());
			pb.assetPool(assetPool);
		}
		{
			pb.reveal(form.getReveal()).revealComment(form.getRevealComment()).currency(form.getCurrency()).incomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getManageRate())) {
				pb.manageRate(ProductDecimalFormat.divide(new BigDecimal(form.getManageRate())));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.fixedManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getFixedManageRate())));
			}
		}
		{
			//募集开始时间类型;募集期:()个自然日;起息日:募集满额后()个自然日 存续期数;存续期单位;存续期:()个自然日
			pb.raiseStartDateType(form.getRaiseStartDateType());
			if(!StringUtil.isEmpty(form.getRaisePeriod())) {
				pb.raisePeriodDays(Integer.valueOf(form.getRaisePeriod()));
			}
			if(!StringUtil.isEmpty(form.getInterestsFirstDate())) {
				pb.interestsFirstDays(Integer.valueOf(form.getInterestsFirstDate()));
			}
			if(!StringUtil.isEmpty(form.getDurationPeriod())) {
				pb.durationPeriodDays(Integer.valueOf(form.getDurationPeriod()));
			}
		}
		{
			//募集开始时间  募集期:()个自然日  起息日:募集满额后()个自然日  存续期:()个自然日
			if(Product.DATE_TYPE_ManualInput.equals(form.getRaiseStartDateType())) {
				java.util.Date raiseStartDate = DateUtil.parseDate(form.getRaiseStartDate(), DateUtil.datePattern);
				pb.raiseStartDate(new Date(raiseStartDate.getTime()));
				
				if(!StringUtil.isEmpty(form.getRaisePeriod())) {
					pb.raiseEndDate(new Date(DateUtil.addDay(raiseStartDate, Integer.valueOf(form.getRaisePeriod())).getTime()));//募集结束时间
					if(!StringUtil.isEmpty(form.getInterestsFirstDate())) {
						pb.setupDate(new Date(DateUtil.addDay(raiseStartDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())).getTime()));//产品成立时间（存续期开始时间）
						if(!StringUtil.isEmpty(form.getDurationPeriod())) {
							pb.durationPeriodEndDate(new Date(DateUtil.addDay(raiseStartDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())+Integer.valueOf(form.getDurationPeriod())).getTime()));//存续期结束时间
							if(!StringUtil.isEmpty(form.getAccrualDate())) {
								java.util.Date repayDate = DateUtil.addDay(raiseStartDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())+Integer.valueOf(form.getDurationPeriod())+Integer.valueOf(form.getAccrualDate()));
								//到期最晚还本付息日 指存续期结束后的还本付息最迟发生在存续期后的第X个自然日的23:59:59为止
								pb.repayDate(new Date(repayDate.getTime()));//到期还款时间
							}
						}
					}
				}
			}
		}
		{
			// 年化收益
			pb.expAror(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				pb.expArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			} else {
				pb.expArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpArorSec())));
			}
		}
		{
			if(!StringUtil.isEmpty(form.getRaisedTotalNumber())) {
				pb.raisedTotalNumber(new BigDecimal(form.getRaisedTotalNumber()));
			}
			if(!StringUtil.isEmpty(form.getInvestMin())) {
				pb.investMin(new BigDecimal(form.getInvestMin()));
			}
			
			if(!StringUtil.isEmpty(form.getInvestMax())) {
				pb.investMax(new BigDecimal(form.getInvestMax()));
			}
//			if(!StringUtil.isEmpty(form.getPurchaseLimit())) {
//				pb.purchaseLimit(Long.valueOf(form.getPurchaseLimit()));
//			}
			if(!StringUtil.isEmpty(form.getInvestAdditional())) {
				pb.investAdditional(new BigDecimal(form.getInvestAdditional()));
			}
			if(!StringUtil.isEmpty(form.getNetUnitShare())) {
				pb.netUnitShare(new BigDecimal(form.getNetUnitShare()));
			}
			if(!StringUtil.isEmpty(form.getAccrualDate())) {
				pb.accrualRepayDays(Integer.valueOf(form.getAccrualDate()));
			}
		}
		{
			pb.isOpenPurchase(Product.YES).isOpenRemeed(Product.NO);
		}
		{
			pb.investComment(form.getInvestComment()).instruction(form.getInstruction()).riskLevel(form.getRiskLevel()).stems(Product.STEMS_Userdefine).auditState(Product.AUDIT_STATE_Nocommit);
		}
		{
			// 其他字段 初始化默认值s
			pb.operator(operator).publisher(operator).updateTime(now).createTime(now);
		}
		// 附件文件
		List<SaveFileForm> fileForms = null;
		String fkey = StringUtil.uuid();
		{
			if (StringUtil.isEmpty(form.getFiles())) {
				pb.fileKeys(StringUtil.EMPTY);
			} else {
				pb.fileKeys(fkey);
				fileForms = JSON.parseArray(form.getFiles(), SaveFileForm.class);
			}
		}
		//投资协议书
		List<SaveFileForm> investFileForm = null;
		String investFileKey = StringUtil.uuid();
		{
			if (StringUtil.isEmpty(form.getInvestFile())) {
				pb.investFileKey(StringUtil.EMPTY);
			} else {
				pb.investFileKey(investFileKey);
				investFileForm = JSON.parseArray(form.getInvestFile(), SaveFileForm.class);
			}
		}
		
		
		Product p = pb.build();
		p = this.productDao.save(p);
			
		// 附件文件
		this.fileService.save(fileForms, fkey, File.CATE_User, operator);
		//投资协议书
		this.fileService.save(investFileForm, investFileKey, File.CATE_User, operator);
		
		if(form.getChannels()!=null && form.getChannels().length>0) {
			List<String> channelOids = new ArrayList<String>();
			for(String channelOid : form.getChannels()) {
				channelOids.add(channelOid);
			}
			this.productChannelService.saveApplyChannel(operator, p.getOid(), channelOids);
		}
		
		return response;
	}
	
	@Transactional
	public BaseResp saveCurrent(SaveCurrentProductForm form, String operator) throws ParseException {

		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());

		Product.ProductBuilder pb = Product.builder().oid(StringUtil.uuid());
		{
			pb.code(form.getCode()).name(form.getName()).fullName(form.getFullName()).administrator(form.getAdministrator()).state(Product.STATE_Create).isDeleted(Product.NO);
		}
		{
			// 产品类型 
			Dict type = this.dictService.get(form.getTypeOid());
			pb.type(type);
			// 收益结转周期
			pb.accrualCycleOid(form.getAccrualCycleOid());
			// 付利方式
			pb.payModeOid(form.getPayModeOid());
			if(!StringUtil.isEmpty(form.getPayModeDate())) {
				pb.payModeDay(Integer.valueOf(form.getPayModeDate()));
			}
		}
		if(!StringUtil.isEmpty(form.getAssetPoolOid())) {
			AssetPoolEntity assetPool = assetPoolDao.findOne(form.getAssetPoolOid());
			pb.assetPool(assetPool);
		}
		{
			pb.reveal(form.getReveal()).revealComment(form.getRevealComment()).currency(form.getCurrency()).incomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getManageRate())) {
				pb.manageRate(ProductDecimalFormat.divide(new BigDecimal(form.getManageRate())));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				pb.fixedManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getFixedManageRate())));
			}
		}
		{
			//产品成立时间类型;起息日;锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
			//申购确认日:()个;申购确认日类型:自然日或交易日
			//赎回确认日:()个;赎回确认日类型:自然日或交易日
			pb.setupDateType(form.getSetupDateType());
			if(!StringUtil.isEmpty(form.getInterestsDate())) {
				pb.interestsFirstDays(Integer.valueOf(form.getInterestsDate()));
			}
			if(!StringUtil.isEmpty(form.getLockPeriod())) {
				pb.lockPeriodDays(Integer.valueOf(form.getLockPeriod()));
			}
			
			if(!StringUtil.isEmpty(form.getPurchaseConfirmDate())) {
				pb.purchaseConfirmDays(Integer.valueOf(form.getPurchaseConfirmDate()));
			}
			
			if(!StringUtil.isEmpty(form.getRedeemConfirmDate())) {
				pb.redeemConfirmDays(Integer.valueOf(form.getRedeemConfirmDate()));
			}
			pb.purchaseConfirmDaysType(form.getPurchaseConfirmDateType())
			.redeemConfirmDaysType(form.getRedeemConfirmDateType());
			//产品成立时间（存续期开始时间）
			
			if(Product.DATE_TYPE_ManualInput.equals(form.getSetupDateType())) {
				java.util.Date setupDate = DateUtil.parseDate(form.getSetupDate(), DateUtil.datePattern);
				pb.setupDate(new Date(setupDate.getTime()));
			}
		}
		{
			// 年化收益
			pb.expAror(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				pb.expArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			} else {
				pb.expArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpArorSec())));
			}
		}
		{
			if(!StringUtil.isEmpty(form.getInvestMin())) {
				pb.investMin(new BigDecimal(form.getInvestMin()));
			}
			
			if(!StringUtil.isEmpty(form.getInvestMax())) {
				pb.investMax(new BigDecimal(form.getInvestMax()));
			}
//			if(!StringUtil.isEmpty(form.getPurchaseLimit())) {
//				pb.purchaseLimit(Long.valueOf(form.getPurchaseLimit()));
//			}
			if(!StringUtil.isEmpty(form.getInvestAdditional())) {
				pb.investAdditional(new BigDecimal(form.getInvestAdditional()));
			}
			if(!StringUtil.isEmpty(form.getNetUnitShare())) {
				pb.netUnitShare(new BigDecimal(form.getNetUnitShare()));
			}
			if(!StringUtil.isEmpty(form.getNetMaxRredeemDay())) {
				pb.netMaxRredeemDay(new BigDecimal(form.getNetMaxRredeemDay()));
			}
			if(!StringUtil.isEmpty(form.getMinRredeem())) {
				pb.minRredeem(new BigDecimal(form.getMinRredeem()));
			}
		}
		{
			pb.isOpenPurchase(Product.YES).isOpenRemeed(Product.YES);
		}
		{
			pb.investComment(form.getInvestComment()).instruction(form.getInstruction()).riskLevel(form.getRiskLevel()).stems(Product.STEMS_Userdefine).auditState(Product.AUDIT_STATE_Nocommit);
		}
		{
			// 其他字段 初始化默认值s
			pb.operator(operator).publisher(operator).updateTime(now).createTime(now);
		}
		// 附件文件
		List<SaveFileForm> fileForms = null;
		String fkey = StringUtil.uuid();
		{
			if (StringUtil.isEmpty(form.getFiles())) {
				pb.fileKeys(StringUtil.EMPTY);
			} else {
				pb.fileKeys(fkey);
				fileForms = JSON.parseArray(form.getFiles(), SaveFileForm.class);
			}
		}
		
		//投资协议书
		List<SaveFileForm> investFileForm = null;
		String investFileKey = StringUtil.uuid();
		{
			if (StringUtil.isEmpty(form.getInvestFile())) {
				pb.investFileKey(StringUtil.EMPTY);
			} else {
				pb.investFileKey(investFileKey);
				investFileForm = JSON.parseArray(form.getInvestFile(), SaveFileForm.class);
			}
		}
		
		Product p = pb.build();
		p = this.productDao.save(p);
			
		// 附件文件
		this.fileService.save(fileForms, fkey, File.CATE_User, operator);
		//投资协议书
		this.fileService.save(investFileForm, investFileKey, File.CATE_User, operator);
		
		if(form.getChannels()!=null && form.getChannels().length>0) {
			List<String> channelOids = new ArrayList<String>();
			for(String channelOid : form.getChannels()) {
				channelOids.add(channelOid);
			}
			this.productChannelService.saveApplyChannel(operator, p.getOid(), channelOids);
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
		
		//面向渠道的属性，同一产品在不同渠道可以为不同状态，同一产品，只要有一个渠道销售状态为“渠道X待上架”，该产品已录入详情字段就不可修改。
		List<ProductChannel> pcs = this.productChannelService.queryProductChannels(oid);
		if(pcs!=null && pcs.size()>0) {
			for(ProductChannel pc : pcs) {
				if(ProductChannel.MARKET_STATE_Shelfing.equals(pc.getMarketState()) 
						|| ProductChannel.MARKET_STATE_Onshelf.equals(pc.getMarketState())) {
					throw AMPException.getException(90007);
				}
			}
		}
		
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
		if(!StringUtil.isEmpty(form.getAssetPoolOid())) {
			AssetPoolEntity assetPool = assetPoolDao.findOne(form.getAssetPoolOid());
			product.setAssetPool(assetPool);
		}
		{
			product.setCode(form.getCode());
			product.setName(form.getName());
			product.setFullName(form.getFullName());
			product.setAdministrator(form.getAdministrator());
			product.setState(Product.STATE_Update);
			product.setReveal(form.getReveal());
			product.setRevealComment(form.getRevealComment());
			product.setCurrency(form.getCurrency());
			product.setIncomeCalcBasis(form.getIncomeCalcBasis());
		}
		{
			if (!StringUtil.isEmpty(form.getManageRate())) {
				product.setManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getManageRate())));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setFixedManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getFixedManageRate())));
			}
		}
		{
			// 年化收益
			product.setExpAror(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				product.setExpArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			} else {
				product.setExpArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpArorSec())));
			}
		}
		{
			//募集开始时间类型;募集期:()个自然日;起息日:募集满额后()个自然日;存续期:()个自然日
			product.setRaiseStartDateType(form.getRaiseStartDateType());
			if(!StringUtil.isEmpty(form.getRaisePeriod())) {
				product.setRaisePeriodDays(Integer.valueOf(form.getRaisePeriod()));
			}
			if(!StringUtil.isEmpty(form.getInterestsFirstDate())) {
				product.setInterestsFirstDays(Integer.valueOf(form.getInterestsFirstDate()));
			}
			if(!StringUtil.isEmpty(form.getDurationPeriod())) {
				product.setDurationPeriodDays(Integer.valueOf(form.getDurationPeriod()));
			}
		}
		if(Product.DATE_TYPE_FirstRackTime.equals(form.getRaiseStartDateType())) {
			product.setRaiseStartDate(null);
		} else {
			java.util.Date raiseStatrtDate = DateUtil.parseDate(form.getRaiseStartDate(), DateUtil.datePattern);
			product.setRaiseStartDate(new Date(raiseStatrtDate.getTime()));
			
			if(!StringUtil.isEmpty(form.getRaisePeriod())) {
				product.setRaiseEndDate(new Date(DateUtil.addDay(raiseStatrtDate, Integer.valueOf(form.getRaisePeriod())).getTime()));//募集结束时间
				if(!StringUtil.isEmpty(form.getInterestsFirstDate())) {
					product.setSetupDate(new Date(DateUtil.addDay(raiseStatrtDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())).getTime()));//产品成立时间（存续期开始时间）
					if(!StringUtil.isEmpty(form.getDurationPeriod())) {
						product.setDurationPeriodEndDate(new Date(DateUtil.addDay(raiseStatrtDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())+Integer.valueOf(form.getDurationPeriod())).getTime()));//存续期结束时间
						if(!StringUtil.isEmpty(form.getAccrualDate())) {
							//到期最晚还本付息日 指存续期结束后的还本付息最迟发生在存续期后的第X个自然日的23:59:59为止
							java.util.Date repayDate = DateUtil.addDay(raiseStatrtDate, Integer.valueOf(form.getRaisePeriod())+Integer.valueOf(form.getInterestsFirstDate())+Integer.valueOf(form.getDurationPeriod())+Integer.valueOf(form.getAccrualDate()));
							product.setRepayDate(new Date(repayDate.getTime()));//到期还款时间
						}
					}
				}
			}
		}
		{
			if(!StringUtil.isEmpty(form.getRaisedTotalNumber())) {
				product.setRaisedTotalNumber(new BigDecimal(form.getRaisedTotalNumber()));
			}
			if(!StringUtil.isEmpty(form.getInvestMin())) {
				product.setInvestMin(new BigDecimal(form.getInvestMin()));
			}
			
			if(!StringUtil.isEmpty(form.getInvestMax())) {
				product.setInvestMax(new BigDecimal(form.getInvestMax()));
			}
//			if(!StringUtil.isEmpty(form.getPurchaseLimit())) {
//				product.setPurchaseLimit(Long.valueOf(form.getPurchaseLimit()));
//			}
			if(!StringUtil.isEmpty(form.getInvestAdditional())) {
				product.setInvestAdditional(new BigDecimal(form.getInvestAdditional()));
			}
			if(!StringUtil.isEmpty(form.getNetUnitShare())) {
				product.setNetUnitShare(new BigDecimal(form.getNetUnitShare()));
			}
			if(!StringUtil.isEmpty(form.getAccrualDate())) {
				product.setAccrualRepayDays(Integer.valueOf(form.getAccrualDate()));
			}
		}
		{
			product.setInvestComment(form.getInvestComment());
			product.setInstruction(form.getInstruction());
			product.setRiskLevel(form.getRiskLevel());
			// 其它：修改时间、操作人
			product.setOperator(operator);
			product.setUpdateTime(now);
		}
		
		//附件文件
		List<SaveFileForm> fileForms = null;
		if (!StringUtil.isEmpty(form.getFiles())) {
			fileForms = JSON.parseArray(form.getFiles(), SaveFileForm.class);
		}
		String fkey = null;
		if (StringUtil.isEmpty(product.getFileKeys())) {
			fkey = StringUtil.uuid();
			if(fileForms!=null && fileForms.size()>0) {
				product.setFileKeys(fkey);
			}
		} else {
			fkey = product.getFileKeys();
			if(fileForms==null || fileForms.size()==0) {
				product.setFileKeys(StringUtil.EMPTY);
			}
		}
		
		//投资协议书
		List<SaveFileForm> investFileForm = null;
		if (!StringUtil.isEmpty(form.getInvestFile())) {
			investFileForm = JSON.parseArray(form.getInvestFile(), SaveFileForm.class);
		}
		String investFileKey = null;
		if (StringUtil.isEmpty(product.getInvestFileKey())) {
			investFileKey = StringUtil.uuid();
			if(investFileForm!=null && investFileForm.size()>0) {
				product.setInvestFileKey(investFileKey);
			}
		} else {
			investFileKey = product.getInvestFileKey();
			if(investFileForm==null || investFileForm.size()==0) {
				product.setInvestFileKey(StringUtil.EMPTY);
			}
		}
		
		// 更新产品
		product = this.productDao.saveAndFlush(product);
		{
			// 附件文件
			this.fileService.save(fileForms, fkey, File.CATE_User, operator);
			//投资协议书
			this.fileService.save(investFileForm, investFileKey, File.CATE_User, operator);
		}
		
		List<String> channelOids = new ArrayList<String>();
		if(form.getChannels()!=null && form.getChannels().length>0) {
			for(String channelOid : form.getChannels()) {
				channelOids.add(channelOid);
			}
		}
		this.productChannelService.saveApplyChannel(operator, product.getOid(), channelOids);
		
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
		if(!StringUtil.isEmpty(form.getAssetPoolOid())) {
			AssetPoolEntity assetPool = assetPoolDao.findOne(form.getAssetPoolOid());
			product.setAssetPool(assetPool);
		}
		{
			product.setCode(form.getCode());
			product.setName(form.getName());
			product.setFullName(form.getFullName());
			product.setAdministrator(form.getAdministrator());
			product.setState(Product.STATE_Update);
			product.setReveal(form.getReveal());
			product.setRevealComment(form.getRevealComment());
			product.setCurrency(form.getCurrency());
			product.setIncomeCalcBasis(form.getIncomeCalcBasis());
		}	
		
		{
			//收益结转周期
			product.setAccrualCycleOid(form.getAccrualCycleOid());
			// 付利方式
			product.setPayModeOid(form.getPayModeOid());
			if(!StringUtil.isEmpty(form.getPayModeDate())) {
				product.setPayModeDay(Integer.valueOf(form.getPayModeDate()));
			}
		}
		{
			if (!StringUtil.isEmpty(form.getManageRate())) {
				product.setManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getManageRate())));
			}
			if (!StringUtil.isEmpty(form.getFixedManageRate())) {
				product.setFixedManageRate(ProductDecimalFormat.divide(new BigDecimal(form.getFixedManageRate())));
			}
		}
		{
			// 年化收益
			product.setExpAror(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			if (StringUtil.isEmpty(form.getExpArorSec()) || new BigDecimal(form.getExpArorSec()).equals(BigDecimal.ZERO)) {
				product.setExpArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpAror())));
			} else {
				product.setExpArorSec(ProductDecimalFormat.divide(new BigDecimal(form.getExpArorSec())));
			}
		}
		{
			product.setSetupDateType(form.getSetupDateType());
			if(!StringUtil.isEmpty(form.getInterestsDate())) {
				product.setInterestsFirstDays(Integer.valueOf(form.getInterestsDate()));
			}
			if(!StringUtil.isEmpty(form.getLockPeriod())) {
				product.setLockPeriodDays(Integer.valueOf(form.getLockPeriod()));
			}
			if(!StringUtil.isEmpty(form.getPurchaseConfirmDate())) {
				product.setPurchaseConfirmDays(Integer.valueOf(form.getPurchaseConfirmDate()));
			}
			product.setPurchaseConfirmDaysType(form.getPurchaseConfirmDateType());
			
			if(!StringUtil.isEmpty(form.getRedeemConfirmDate())) {
				product.setRedeemConfirmDays(Integer.valueOf(form.getRedeemConfirmDate()));
			}
			product.setRedeemConfirmDaysType(form.getRedeemConfirmDateType());
			
			if(Product.DATE_TYPE_FirstRackTime.equals(form.getSetupDateType())) {
				form.setSetupDate(null);
			} else {
			//产品成立时间（存续期开始时间）
				java.util.Date setupDate = DateUtil.parseDate(form.getSetupDate(), DateUtil.datePattern);
				product.setSetupDate(new Date(setupDate.getTime()));
			}
		}
		{
			if(!StringUtil.isEmpty(form.getInvestMin())) {
				product.setInvestMin(new BigDecimal(form.getInvestMin()));
			}
			if(!StringUtil.isEmpty(form.getInvestMax())) {
				product.setInvestMax(new BigDecimal(form.getInvestMax()));
			}
//			if(!StringUtil.isEmpty(form.getPurchaseLimit())) {
//				product.setPurchaseLimit(Long.valueOf(form.getPurchaseLimit()));
//			}
			if(!StringUtil.isEmpty(form.getInvestAdditional())) {
				product.setInvestAdditional(new BigDecimal(form.getInvestAdditional()));
			}
			if(!StringUtil.isEmpty(form.getNetUnitShare())) {
				product.setNetUnitShare(new BigDecimal(form.getNetUnitShare()));
			}
			if(!StringUtil.isEmpty(form.getNetMaxRredeemDay())) {
				product.setNetMaxRredeemDay(new BigDecimal(form.getNetMaxRredeemDay()));
			}
			if(!StringUtil.isEmpty(form.getMinRredeem())) {
				product.setMinRredeem(new BigDecimal(form.getMinRredeem()));
			}
		}
		{
			product.setInvestComment(form.getInvestComment());
			product.setInstruction(form.getInstruction());
			product.setRiskLevel(form.getRiskLevel());
			// 其它：修改时间、操作人
			product.setOperator(operator);
			product.setUpdateTime(now);
		}
		
		
		//附件文件
		List<SaveFileForm> fileForms = null;
		if (!StringUtil.isEmpty(form.getFiles())) {
			fileForms = JSON.parseArray(form.getFiles(), SaveFileForm.class);
		}
		String fkey = null;
		if (StringUtil.isEmpty(product.getFileKeys())) {
			fkey = StringUtil.uuid();
			if(fileForms!=null && fileForms.size()>0) {
				product.setFileKeys(fkey);
			}
		} else {
			fkey = product.getFileKeys();
			if(fileForms==null || fileForms.size()==0) {
				product.setFileKeys(StringUtil.EMPTY);
			}
		}
		//投资协议书
		List<SaveFileForm> investFileForm = null;
		if (!StringUtil.isEmpty(form.getInvestFile())) {
			investFileForm = JSON.parseArray(form.getInvestFile(), SaveFileForm.class);
		}
		String investFileKey = null;
		if (StringUtil.isEmpty(product.getInvestFileKey())) {
			investFileKey = StringUtil.uuid();
			if(investFileForm!=null && investFileForm.size()>0) {
				product.setInvestFileKey(investFileKey);
			}
		} else {
			investFileKey = product.getInvestFileKey();
			if(investFileForm==null || investFileForm.size()==0) {
				product.setInvestFileKey(StringUtil.EMPTY);
			}
		}
				
		// 更新产品
		product = this.productDao.saveAndFlush(product);

		{
			// 附件文件
			this.fileService.save(fileForms, fkey, File.CATE_User, operator);
			//投资协议书
			this.fileService.save(investFileForm, investFileKey, File.CATE_User, operator);
		}
		
		List<String> channelOids = new ArrayList<String>();
		if(form.getChannels()!=null && form.getChannels().length>0) {
			for(String channelOid : form.getChannels()) {
				channelOids.add(channelOid);
			}
		}
		this.productChannelService.saveApplyChannel(operator, product.getOid(), channelOids);
		
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

		Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
		
		FileResp fr = null;
		AdminObj adminObj = null;
		if (!StringUtil.isEmpty(product.getFileKeys())) {
			List<File> files = this.fileService.list(product.getFileKeys(), File.STATE_Valid);
			if (files.size() > 0) {
				pr.setFiles(new ArrayList<FileResp>());
				
				
				for (File file : files) {
					fr = new FileResp(file);
					if(adminObjMap.get(file.getOperator())==null) {
						try {
							adminObj = adminSdk.getAdmin(file.getOperator());
							adminObjMap.put(file.getOperator(),adminObj);
						} catch (Exception e) {
						}
					}
					if(adminObjMap.get(file.getOperator())!=null) {
						fr.setOperator(adminObjMap.get(file.getOperator()).getName());
					}
					pr.getFiles().add(fr);
				}
			}
		}
		
		if (!StringUtil.isEmpty(product.getInvestFileKey())) {
			List<File> files = this.fileService.list(product.getInvestFileKey(), File.STATE_Valid);
			if (files.size() > 0) {
				pr.setInvestFiles(new ArrayList<FileResp>());
				
				for (File file : files) {
					fr = new FileResp(file);
					if(adminObjMap.get(file.getOperator())==null) {
						try {
							adminObj = adminSdk.getAdmin(file.getOperator());
							adminObjMap.put(file.getOperator(),adminObj);
						} catch (Exception e) {
						}
					}
					if(adminObjMap.get(file.getOperator())!=null) {
						fr.setOperator(adminObjMap.get(file.getOperator()).getName());
					}
					pr.getInvestFiles().add(fr);
				}
			}
		}
		
		List<ProductChannel> pcs = productChannelService.queryProductChannels(oid);
		if(pcs!=null && pcs.size()>0) {
			StringBuilder channelNames = new StringBuilder();
			List<String> channelOids =  new ArrayList<String>();
			for(ProductChannel pc : pcs) {
				channelNames.append(pc.getChannel().getChannelName()).append(",");
				channelOids.add(pc.getChannel().getOid());
			}
			pr.setChannelNames(channelNames.substring(0, channelNames.length()-1));
			pr.setChannelOids(channelOids);
		}
		
		List<ProductIncomeReward> pirs = productRewardService.productRewardList(oid);
		if(pirs!=null && pirs.size()>0) {
			List<ProductRewardResp> rewards = new ArrayList<ProductRewardResp>();
			for (ProductIncomeReward pir : pirs) {
				ProductRewardResp pirRep = new ProductRewardResp(pir);
				rewards.add(pirRep);
			}
			pr.setRewards(rewards);
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
			Map<String,List<ProductChannel>> channelNum = this.getProductChannels(cas.getContent());
			
			for (Product p : cas) {
				ProductResp queryRep = new ProductResp(p);
				if(channelNum.get(p.getOid())!=null) {
					queryRep.setChannelNum(channelNum.get(p.getOid()).size());
					for(ProductChannel pc : channelNum.get(p.getOid())) {
						queryRep.setMarketState(pc.getMarketState());
						if(ProductChannel.MARKET_STATE_Onshelf.equals(pc.getMarketState())) {
							break;
						}
					}
				}
				
				rows.add(queryRep);
			}
			pagesRep.setRows(rows);
		}
		pagesRep.setTotal(cas.getTotalElements());
		return pagesRep;
	}
	
	
	private Map<String,List<ProductChannel>> getProductChannels (List<Product> ps){
		List<String> productOids =  new ArrayList<String>();
		for (Product p : ps) {
			productOids.add(p.getOid());
		}
		Map<String,List<ProductChannel>> channelNum = new HashMap<String,List<ProductChannel>>();
		List<ProductChannel> pcs = productChannelService.queryProductChannels(productOids);
		if(pcs!=null && pcs.size()>0) {
			for(ProductChannel pc : pcs) {
				if(channelNum.get(pc.getProduct().getOid())==null) {
					channelNum.put(pc.getProduct().getOid(),new ArrayList<ProductChannel>());
				}
				channelNum.get(pc.getProduct().getOid()).add(pc);
			}
		}
		return channelNum;
	}
	/**
	 * 查询审核中
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public PageResp<ProductLogListResp> auditList(Specification<Product> spec, Pageable pageable) {
		Page<Product> cas = this.productDao.findAll(spec, pageable);
		PageResp<ProductLogListResp> pagesRep = new PageResp<ProductLogListResp>();
		if (cas != null && cas.getContent() != null && cas.getTotalElements() > 0) {
			List<ProductLogListResp> rows = new ArrayList<ProductLogListResp>();
			Map<String,List<ProductChannel>> channelNum = this.getProductChannels(cas.getContent());
			
			Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
			
			for (Product p : cas) {
				ProductLogListResp queryRep = new ProductLogListResp(p);
				if(channelNum.get(p.getOid())!=null) {
					queryRep.setChannelNum(channelNum.get(p.getOid()).size());
				}
				//申请人和申请时间
				ProductLog pl = this.getProductLog(p.getOid(), ProductLog.AUDIT_TYPE_Auditing, ProductLog.AUDIT_STATE_Commited);
					
				if(pl!=null) {
					if(adminObjMap.get(pl.getAuditor())==null) {
						AdminObj adminObj = null;
						try{
							adminObj = adminSdk.getAdmin(pl.getAuditor());
							adminObjMap.put(pl.getAuditor(),adminObj);
						} catch (Exception e) {
						}
						
					}
					if(adminObjMap.get(pl.getAuditor())!=null) {
						queryRep.setApplicant(adminObjMap.get(pl.getAuditor()).getName());
					}
					queryRep.setApplyTime(DateUtil.formatDatetime(pl.getAuditTime().getTime()));
				}
				rows.add(queryRep);
			}
			pagesRep.setRows(rows);
		}
		pagesRep.setTotal(cas.getTotalElements());
		
		return pagesRep;
	}
	
	/**
	 * 查询复核中
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public PageResp<ProductLogListResp> checkList(Specification<Product> spec, Pageable pageable) {
		PageResp<ProductLogListResp> pagesRep = this.auditList(spec, pageable);
		List<ProductLogListResp> rows = pagesRep.getRows();
		
		Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
			
		AdminObj adminObj = null;
		for (ProductLogListResp p : rows) {
			ProductLog pl = this.getProductLog(p.getOid(), ProductLog.AUDIT_TYPE_Auditing, ProductLog.AUDIT_STATE_Approval);
			
			if(pl!=null) {
				//审核人 审核时间
				if(adminObjMap.get(pl.getAuditor())==null) {
					try {
						adminObj = adminSdk.getAdmin(pl.getAuditor());
						adminObjMap.put(pl.getAuditor(),adminObj);
					} catch (Exception e) {
					}
				}
				if(adminObjMap.get(pl.getAuditor())!=null) {
					p.setAuditor(adminObjMap.get(pl.getAuditor()).getName());
				}
				p.setAuditTime(DateUtil.formatDatetime(pl.getAuditTime().getTime()));
			}
		}
		return pagesRep;
	}
	
	/**
	 * 查询准入中
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public PageResp<ProductLogListResp> approveList(Specification<Product> spec, Pageable pageable) {
		PageResp<ProductLogListResp> pagesRep = this.checkList(spec, pageable);
		List<ProductLogListResp> rows = pagesRep.getRows();
		
		Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
		AdminObj adminObj = null;
		for (ProductLogListResp p : rows) {
			ProductLog pl = this.getProductLog(p.getOid(), ProductLog.AUDIT_TYPE_Reviewing, ProductLog.AUDIT_STATE_Approval);
			if(pl!=null) {
				//复核人复核时间
				if(adminObjMap.get(pl.getAuditor())==null) {
					try {
						adminObj = adminSdk.getAdmin(pl.getAuditor());
						adminObjMap.put(pl.getAuditor(),adminObj);
					} catch (Exception e) {
					}
				}
				if(adminObjMap.get(pl.getAuditor())!=null) {
					p.setReviewer(adminObjMap.get(pl.getAuditor()).getName());
				}
				p.setReviewTime(DateUtil.formatDatetime(pl.getAuditTime().getTime()));
			}
		}
		return pagesRep;
	}
	
	/**
	 * 查询productlog
	 * @param oid
	 * @param auditType
	 * @param auditState
	 * @return
	 */
	private ProductLog getProductLog(final String oid, final String auditType, final String auditState) {
		Direction sortDirection = Direction.DESC;
		Sort sort = new Sort(new Order(sortDirection, "auditTime"));
		
		Specification<ProductLog> spec = new Specification<ProductLog>() {
			@Override
			public Predicate toPredicate(Root<ProductLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("product").get("oid").as(String.class), oid)
						, cb.equal(root.get("auditType").as(String.class), auditType)
						, cb.equal(root.get("auditState").as(String.class), auditState));
				
			}
		};
		
		List<ProductLog> pls = this.productLogDao.findAll(spec, sort);
		if(null!=pls && pls.size()>0) {
			return pls.get(0);
		}
		return null;
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
			if(!Product.AUDIT_STATE_Nocommit.equals(product.getAuditState())) {
				throw AMPException.getException(90012);
			}
			product.setState(Product.STATE_Auditing);
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
		
		if(!Product.AUDIT_STATE_Auditing.equals(product.getAuditState())) {
			throw AMPException.getException(90013);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Auditpass);
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
		if(!Product.AUDIT_STATE_Auditing.equals(product.getAuditState())) {
			throw AMPException.getException(90013);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Auditfail);
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
		if(!Product.AUDIT_STATE_Reviewing.equals(product.getAuditState())) {
			throw AMPException.getException(90014);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Reviewpass);
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
		if(!Product.AUDIT_STATE_Reviewing.equals(product.getAuditState())) {
			throw AMPException.getException(90014);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Reviewfail);
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
		if(!Product.AUDIT_STATE_Approvaling.equals(product.getAuditState())) {
			throw AMPException.getException(90015);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Admitpass);
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
		if(!Product.AUDIT_STATE_Approvaling.equals(product.getAuditState())) {
			throw AMPException.getException(90015);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		product.setState(Product.STATE_Admitfail);
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
	
	@Transactional
	public long validateSingle(String attrName, String value, String oid) {
		
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(StringUtil.isEmpty(oid)) {
					return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO),cb.equal(root.get(attrName).as(String.class), value));
				} else {
					return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO),cb.equal(root.get(attrName).as(String.class), value),cb.notEqual(root.get("oid").as(String.class), oid));
				}
			}
		};
		spec = Specifications.where(spec);
		
		return this.productDao.count(spec);
	}
	
}
