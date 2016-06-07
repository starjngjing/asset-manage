package com.guohuai.asset.manage.boot.duration.order;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.pool.CashToolRevenue;
import com.guohuai.asset.manage.boot.cashtool.pool.CashtoolPoolService;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.duration.capital.CapitalService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundService;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeForm;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustService;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustTransEntity;
import com.guohuai.asset.manage.boot.duration.target.TargetService;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.pool.InvestmentPoolService;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--订单服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class OrderService {
	
	/**
	 * 标的类型
	 */
	public static final String FUND 	= "现金类管理工具标的";
	public static final String TRUST 	= "信托（计划）标的";
	
	/**
	 * 操作类型
	 */
	private static final String PURCHASE 	= "purchase";
	private static final String REDEEM 		= "redeem";
	private static final String INCOME 		= "income";
	private static final String TRANSFER	= "transfer";
	
	private static final String APPLY 		= "apply";
	private static final String AUDIT 		= "audit";
	private static final String APPOINTMENT = "appointment";
	private static final String CONFIRM 	= "confirm";
	
	/**
	 * 订单状态
	 */
	private static final String APPLY00 		= "00";	// 申请待审核
	private static final String AUDIT10 		= "10";	// 审核未通过
	private static final String AUDIT11 		= "11";	// 审核通过待预约
	private static final String APPOINTMENT20 	= "20";	// 预约未通过
	private static final String APPOINTMENT21 	= "21";	// 预约通过待确认
	private static final String CONFIRM30 		= "30";	// 确认未通过
	private static final String CONFIRM31 		= "31";	// 确认通过
	
	
	@Autowired
	private FundService fundService;
	@Autowired
	private TrustService trustService;
	@Autowired
	private CapitalService capitalService;
	@Autowired
	private TargetService targetService;
	@Autowired
	private AssetPoolService assetPoolService;
	@Autowired
	private CashtoolPoolService cashtoolPoolService;
	@Autowired
	private InvestmentPoolService investService;

	/**
	 * 货币基金（现金管理工具）申购
	 * @param from
	 * @param uid
	 * @param type
	 * 			申购方式：assetPool（资产池）；order（订单）
	 */
	@Transactional
	public void purchaseForFund(FundForm form, String uid, String type) {
		FundEntity entity = new FundEntity();
		if ("assetPool".equals(type)) {
			entity.setOid(StringUtil.uuid());
			CashTool cashTool = targetService.getCashToolByOid(form.getCashtoolOid());
			entity.setCashTool(cashTool);
			entity.setAssetPoolOid(form.getAssetPoolOid());
			entity.setState(FundEntity.INVESTEND);
			entity.setAmount(BigDecimal.ZERO);
			entity.setFrozenCapital(BigDecimal.ZERO);
			entity.setPurchaseVolume(BigDecimal.ZERO);
			entity.setRedeemVolume(BigDecimal.ZERO);
		} else {
			entity = fundService.getFundByOid(form.getOid());
		}
		entity.setPurchaseVolume(form.getVolume());
		entity.setFrozenCapital(form.getVolume());
		
		fundService.save(entity);
		
		FundOrderEntity order = new FundOrderEntity();
		order.setOid(StringUtil.uuid());
		order.setFundEntity(entity);
//		order.setState(FundOrderEntity.STATE_AUDIT);
		order.setState(APPLY00);
		order.setInvestDate(form.getInvestDate());
		order.setVolume(form.getVolume());
		order.setOptType("purchase");
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
		investService.IncApplyAmount(form.getCashtoolOid(), form.getVolume());
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
				order.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, PURCHASE, APPLY, uid, null);
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 * @param uid
	 */
	@Transactional
	public void redeem(FundForm form, String uid) {
		FundEntity entity = fundService.getFundByOid(form.getOid());
		if ("yes".equals(form.getAllFlag())) {
//			entity.setState(FundEntity.INVESTEND);
			form.setRedeemVolume(entity.getAmount());
		}
		entity.setAmount(entity.getAmount().subtract(form.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		entity.setRedeemVolume(entity.getRedeemVolume().add(form.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		entity.setOnWayCapital(entity.getOnWayCapital().add(form.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		
		fundService.save(entity);
		
		FundOrderEntity order = new FundOrderEntity();
		order.setOid(StringUtil.uuid());
		order.setFundEntity(entity);
		order.setRedeemDate(form.getRedeemDate());
		order.setReturnVolume(form.getReturnVolume());
		order.setOptType("redeem");
//		order.setState(FundOrderEntity.STATE_AUDIT);
		order.setState(APPLY00);
		order.setAllFlag(form.getAllFlag());
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
		investService.IncApplyAmount(entity.getCashTool().getOid(), entity.getRedeemVolume().negate());
		
		// 资金变动记录
		capitalService.capitalFlow(entity.getAssetPoolOid(), entity.getCashTool().getOid(), 
				order.getOid(), FUND, form.getReturnVolume(), BigDecimal.ZERO, REDEEM, APPLY, uid, null);
	}
	
	/**
	 * 货币基金（现金管理工具）申赎审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void auditForFund(FundForm form, String uid) {
		// 录入申购订单的审核信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setAuditVolume(form.getAuditVolume());
		if (FundAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(FundOrderEntity.STATE_APPOINTMENT);
			order.setState(AUDIT11);
		else {
//			order.setState(FundOrderEntity.STATE_FAIL);
			order.setState(AUDIT10);
			
			FundEntity fundEntity = fundService.getFundByOid(order.getFundEntity().getOid());
			fundEntity.setPurchaseVolume(BigDecimal.ZERO);
			fundEntity.setFrozenCapital(BigDecimal.ZERO);
			if ("redeem".equals(order.getOptType())) {
				fundEntity.setAmount(fundEntity.getAmount()
						.add(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setRedeemVolume(fundEntity.getRedeemVolume()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setOnWayCapital(fundEntity.getOnWayCapital()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
			fundService.save(fundEntity);
		}
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
		// 审核记录
		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getOptType()))
			entity.setAuditType(FundAuditEntity.TYPE_AUDIT + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_AUDIT + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getOptType())) {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getAuditVolume(), order.getReturnVolume(), REDEEM, AUDIT, uid, form.getState());
		} else {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getAuditVolume(), order.getVolume(), PURCHASE, AUDIT, uid, form.getState());
		}
	}
	
	/**
	 * 货币基金（现金管理工具）申赎资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void appointmentForFund(FundForm form, String uid) {
		// 录入申购订单的资金预约信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setReserveVolume(form.getReserveVolume());
		if (FundAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(FundOrderEntity.STATE_CONFIRM);
			order.setState(APPOINTMENT21);
		else {
//			order.setState(FundOrderEntity.STATE_FAIL);
			order.setState(APPOINTMENT20);
			
			FundEntity fundEntity = fundService.getFundByOid(order.getFundEntity().getOid());
			fundEntity.setPurchaseVolume(BigDecimal.ZERO);
			fundEntity.setFrozenCapital(BigDecimal.ZERO);
			if ("redeem".equals(order.getOptType())) {
				fundEntity.setAmount(fundEntity.getAmount()
						.add(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setRedeemVolume(fundEntity.getRedeemVolume()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setOnWayCapital(fundEntity.getOnWayCapital()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
			fundService.save(fundEntity);
		}
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);

		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getOptType()))
			entity.setAuditType(FundAuditEntity.TYPE_APPOINTMENT + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_APPOINTMENT + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getOptType())) {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getReserveVolume(), order.getAuditVolume(), REDEEM, APPOINTMENT, uid, form.getState());
		} else {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getReserveVolume(), order.getAuditVolume(), PURCHASE, APPOINTMENT, uid, form.getState());
		}
	}
	
	/**
	 * 货币基金（现金管理工具）申赎订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void orderConfirmForFund(FundForm form, String uid) {
		// 录入申购订单的确认信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setInvestVolume(form.getInvestVolume());
		FundEntity fundEntity = fundService.getFundByOid(order.getFundEntity().getOid());
		if (FundAuditEntity.SUCCESSED.equals(form.getState())) {
//			order.setState(FundOrderEntity.STATE_SUCCESS);
			order.setState(CONFIRM31);
			if ("purchase".equals(order.getOptType())) {
				fundEntity.setAmount(fundEntity.getAmount().add(form.getInvestVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setState(FundEntity.INVESTING);
				
				investService.IncHoldAmount(fundEntity.getCashTool().getOid(), form.getInvestVolume());
			} else {
				investService.IncHoldAmount(fundEntity.getCashTool().getOid(), form.getInvestVolume().negate());
			}
			
		} else {
//			order.setState(FundOrderEntity.STATE_FAIL);
			order.setState(CONFIRM30);
			if ("redeem".equals(order.getOptType())) {
				fundEntity.setAmount(fundEntity.getAmount()
						.add(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setRedeemVolume(fundEntity.getRedeemVolume()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
				fundEntity.setOnWayCapital(fundEntity.getOnWayCapital()
						.subtract(order.getReturnVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
		}
		fundEntity.setPurchaseVolume(BigDecimal.ZERO);
		fundEntity.setFrozenCapital(BigDecimal.ZERO);
		fundEntity.setRedeemVolume(BigDecimal.ZERO);
		fundEntity.setOnWayCapital(BigDecimal.ZERO);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(fundEntity);
		fundService.save(order);

		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getOptType()))
			entity.setAuditType(FundAuditEntity.TYPE_CONFIRM + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_CONFIRM + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getOptType())) {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getInvestVolume(), order.getReserveVolume(), REDEEM, CONFIRM, uid, form.getState());
		} else {
			capitalService.capitalFlow(order.getFundEntity().getAssetPoolOid(), order.getFundEntity().getCashTool().getOid(), 
					order.getOid(), FUND, form.getInvestVolume(), order.getReserveVolume(), PURCHASE, CONFIRM, uid, form.getState());
		}
	}
	
	/**
	 * 信托（计划）申购
	 * @param from
	 * @param uid
	 */
	@Transactional
	public void purchaseForTrust(TrustForm form, String uid) {
		TrustOrderEntity order = new TrustOrderEntity();

		order.setOid(StringUtil.uuid());
		Investment target = targetService.getInvestmentByOid(form.getTargetOid());
//		order.setTargetOid(form.getTargetOid());
		order.setTarget(target);
//		order.setTargetName(form.getTargetName());
//		order.setTargetType(form.getTargetType());
		order.setAssetPoolOid(form.getAssetPoolOid());
		order.setInvestDate(form.getInvestDate());
//		order.setIncomeDate(form.getIncomeDate());
		order.setApplyVolume(form.getVolume());
//		order.setIncomeRate(form.getIncomeRate());
//		order.setSubjectRating(form.getSubjectRating());
		order.setProfitType(form.getProfitType());
//		order.setState(TrustOrderEntity.STATE_AUDIT);
		order.setState(APPLY00);
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
/*		TrustEntity trustEntity = new TrustEntity();
		trustEntity.setOid(StringUtil.uuid());
//		trustEntity.setTargetOid(form.getTargetOid());
		trustEntity.getTarget().setOid(form.getTargetOid());
		trustEntity.setOrderOid(order.getOid());
		trustEntity.setAssetPoolOid(form.getAssetPoolOid());
		trustEntity.setPurchase(PURCHASE);
		trustEntity.setIncomeDate(form.getIncomeDate());
		trustEntity.setState(TrustOrderEntity.STATE_AUDIT);
		trustEntity.setApplyAmount(form.getVolume());
		
		trustService.save(trustEntity);*/
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, PURCHASE, APPLY, uid, null);
	}
	
	/**
	 * 信托（计划）申购审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void auditForTrust(TrustForm form, String uid) {
		// 录入申购订单的审核信息
		TrustOrderEntity order = trustService.getTrustOrderByOid(form.getOid());
		order.setAuditVolume(form.getAuditVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(TrustOrderEntity.STATE_APPOINTMENT);
			order.setState(AUDIT11);
		else
//			order.setState(TrustOrderEntity.STATE_FAIL);
			order.setState(AUDIT10);
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getAssetPoolOid(), order.getTarget().getOid(), 
				order.getOid(), TRUST, form.getAuditVolume(), order.getApplyVolume(), PURCHASE, AUDIT, uid, form.getState());
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void appointmentForTrust(TrustForm form, String uid) {
		// 录入申购订单的资金预约信息
		TrustOrderEntity order = trustService.getTrustOrderByOid(form.getOid());
		order.setReserveVolume(form.getReserveVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(TrustOrderEntity.STATE_CONFIRM);
			order.setState(APPOINTMENT21);
		else
//			order.setState(TrustOrderEntity.STATE_FAIL);
			order.setState(APPOINTMENT20);
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_APPOINTMENT + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getAssetPoolOid(), order.getTarget().getOid(), 
				order.getOid(), TRUST, form.getReserveVolume(), order.getAuditVolume(), PURCHASE, APPOINTMENT, uid, form.getState());
	}
	
	/**
	 * 信托（计划）订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void orderConfirmForTrust(TrustForm form, String uid) {
		// 录入申购订单的确认信息
		TrustOrderEntity order = trustService.getTrustOrderByOid(form.getOid());
		order.setInvestVolume(form.getInvestVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState())) {
//			order.setState(TrustOrderEntity.STATE_SUCCESS);
			order.setState(CONFIRM31);
			
			TrustEntity trustEntity = new TrustEntity();
			trustEntity.setOid(StringUtil.uuid());
//			Investment target = new Investment();
//			target.setOid(order.getTargetOid());
			trustEntity.setTarget(order.getTarget());
			trustEntity.setOrderOid(order.getOid());
			trustEntity.setAssetPoolOid(order.getAssetPoolOid());
			trustEntity.setPurchase(PURCHASE);
//			trustEntity.setIncomeDate(form.getIncomeDate());
			trustEntity.setState(TrustEntity.INVESTING);
			trustEntity.setApplyAmount(form.getInvestVolume());
			trustEntity.setInvestAmount(form.getInvestVolume());
			trustEntity.setInvestDate(order.getInvestDate());
			trustEntity.setProfitType(order.getProfitType());
			
			trustService.save(trustEntity);
		} else
//			order.setState(TrustOrderEntity.STATE_FAIL);
			order.setState(CONFIRM30);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getAssetPoolOid(), order.getTarget().getOid(), 
				order.getOid(), TRUST, form.getInvestVolume(), order.getReserveVolume(), PURCHASE, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 信托（计划）本息兑付订单
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void applyForIncome(TrustForm form, String uid) {
		TrustEntity trustEntity = trustService.getTrustByOid(form.getOid());
		
		TrustIncomeEntity order = new TrustIncomeEntity();
		order.setOid(StringUtil.uuid());
		order.setTrustEntity(trustEntity);
//		order.setTargetOid(trustEntity.getTarget().getOid());
//		order.setTargetName(trustEntity.getTarget().getName());
//		order.setTargetType(trustEntity.getTarget().getType());
		order.setSeq(form.getSeq());
//		order.setState(TrustIncomeEntity.STATE_AUDIT);
		order.setState(APPLY00);
//		order.setIncomeRate(form.getIncomeRate());
		order.setIncome(form.getIncome());
		order.setIncomeDate(form.getIncomeDate());
//		order.setSubjectRating(form.getSubjectRating());
		// 是否兑付本金
		if (null != form.getCapital()) {
			order.setCapital(form.getCapital());
		} else {
			order.setCapital(BigDecimal.ZERO);
		}
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(trustEntity.getAssetPoolOid(), trustEntity.getTarget().getOid(), 
				order.getOid(), TRUST, form.getIncome(), BigDecimal.ZERO, INCOME, APPLY, uid, null);
	}
	
	/**
	 * 信托（计划）本息兑付审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void auditForIncome(TrustForm form, String uid) {
		// 录入申购订单的审核信息
		TrustIncomeEntity order = trustService.getTrustIncomeOrderByOid(form.getOid());
		order.setAuditIncome(form.getAuditVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(TrustIncomeEntity.STATE_CONFIRM);
			order.setState(AUDIT11);
		else
//			order.setState(TrustIncomeEntity.STATE_FAIL);
			order.setState(AUDIT10);
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(order.getTrustEntity().getOrderOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getTrustEntity().getAssetPoolOid(), order.getTrustEntity().getTarget().getOid(), 
				order.getOid(), TRUST, form.getAuditVolume(), order.getIncome(), INCOME, AUDIT, uid, form.getState());
	}
	
	/**
	 * 信托（计划）本息兑付确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void orderConfirmForIncome(TrustForm form, String uid) {
		// 录入申购订单的确认信息
		TrustIncomeEntity order = trustService.getTrustIncomeOrderByOid(form.getOid());
		order.setInvestIncome(form.getInvestVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState())) {
//			order.setState(TrustOrderEntity.STATE_SUCCESS);
			order.setState(CONFIRM31);
			
			TrustEntity trustEntity = trustService.getTrustByOid(order.getTrustEntity().getOid());
			if (!BigDecimal.ZERO.equals(order.getCapital())) {
				trustEntity.setInvestAmount(BigDecimal.ZERO);
				trustEntity.setState(TrustEntity.INVESTEND);
			}
			trustEntity.setTotalProfit(form.getInvestVolume());
			trustService.save(trustEntity);
		} else
//			order.setState(TrustOrderEntity.STATE_FAIL);
			order.setState(CONFIRM30);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(order.getTrustEntity().getOrderOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getTrustEntity().getAssetPoolOid(), order.getTrustEntity().getTarget().getOid(), 
				order.getOid(), TRUST, form.getInvestVolume(), order.getAuditIncome(), INCOME, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	@Transactional
	public void applyForTransfer(TrustForm form, String uid) {
		TrustEntity trustEntity = trustService.getTrustByOid(form.getOid());
		
		TrustTransEntity order = new TrustTransEntity();
		order.setOid(StringUtil.uuid());
//		order.setTargetOid(form.getTargetOid());
//		order.setTargetOid(trustEntity.getTarget().getOid());
//		order.setTargetName(trustEntity.getTarget().getName());
//		order.setTargetType(trustEntity.getTarget().getType());
		order.setTranDate(form.getTranDate());
		order.setTranVolume(form.getTranVolume());
		order.setTranCash(form.getTranCash());
		trustEntity.setTransOutAmount(trustEntity.getTransOutAmount().add(form.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		trustEntity.setTransOutFee(trustEntity.getTransOutFee().add(form.getInvestVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		trustEntity.setInvestAmount(trustEntity.getInvestAmount()
				.subtract(form.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
		trustService.save(trustEntity);
		
		order.setTrustEntity(trustEntity);
//		order.setSubjectRating(form.getSubjectRating());
//		order.setState(TrustTransEntity.STATE_AUDIT);
		order.setState(APPLY00);
		order.setCreater(uid);
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(trustEntity.getAssetPoolOid(), trustEntity.getTarget().getOid(), 
				order.getOid(), TRUST, form.getTranVolume(), BigDecimal.ZERO, TRANSFER, APPLY, uid, null);
	}
	
	/**
	 * 信托（计划）转让审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void auditForTransfer(TrustForm form, String uid) {
		// 录入申购订单的审核信息
		TrustTransEntity order = trustService.getTrustTransOrderByOid(form.getOid());
		order.setAuditVolume(form.getAuditVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
//			order.setState(TrustTransEntity.STATE_CONFIRM);
			order.setState(AUDIT11);
		else {
//			order.setState(TrustTransEntity.STATE_FAIL);
			order.setState(AUDIT10);
			TrustEntity trustEntity = order.getTrustEntity();
			trustEntity.setInvestAmount(trustEntity.getInvestAmount()
					.add(order.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			trustEntity.setTransOutAmount(trustEntity.getTransOutAmount()
					.subtract(order.getTranVolume().setScale(4, BigDecimal.ROUND_HALF_UP)));
			trustService.save(trustEntity);
		}
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(order.getTrustEntity().getOrderOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getTrustEntity().getAssetPoolOid(), order.getTrustEntity().getTarget().getOid(), 
				order.getOid(), TRUST, form.getAuditVolume(), order.getTranVolume(), TRANSFER, AUDIT, uid, form.getState());
	}
	
	/**
	 * 信托（计划）转让确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	@Transactional
	public void orderConfirmForTransfer(TrustForm form, String uid) {
		// 录入申购订单的确认信息
		TrustTransEntity order = trustService.getTrustTransOrderByOid(form.getOid());
		order.setInvestVolume(form.getInvestVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState())) {
//			order.setState(TrustTransEntity.STATE_SUCCESS);
			order.setState(CONFIRM31);
			
			TrustEntity trustEntity = trustService.getTrustByOid(order.getTrustEntity().getOid());
//			trustEntity.setInvestAmount(trustEntity.getInvestAmount().subtract(order.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
//			trustEntity.setTransOutAmount(trustEntity.getTransOutAmount().add(order.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
//			trustEntity.setTransOutFee(trustEntity.getTransOutFee().add(form.getInvestVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			if (trustEntity.getInvestAmount().compareTo(BigDecimal.ZERO) < 1)
				trustEntity.setState(TrustEntity.INVESTEND);
			trustService.save(trustEntity);
		} else {
//			order.setState(TrustTransEntity.STATE_FAIL);
			order.setState(CONFIRM30);
			TrustEntity trustEntity = order.getTrustEntity();
			trustEntity.setInvestAmount(trustEntity.getInvestAmount()
					.add(order.getTranVolume()).setScale(4, BigDecimal.ROUND_HALF_UP));
			trustEntity.setTransOutAmount(trustEntity.getTransOutAmount()
					.subtract(order.getTranVolume().setScale(4, BigDecimal.ROUND_HALF_UP)));
			trustService.save(trustEntity);
		}
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(order.getTrustEntity().getOrderOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(order.getTrustEntity().getAssetPoolOid(), order.getTrustEntity().getTarget().getOid(), 
				order.getOid(), TRUST, form.getInvestVolume(), order.getAuditVolume(), TRANSFER, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 根据订单oid获取 申购 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustOrderEntity getTrustPurchaseOrderByOid(String oid) {
		TrustOrderEntity entity = trustService.getTrustOrderByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 本息兑付 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustIncomeEntity getTargetIncomeOrderByOid(String oid) {
		TrustIncomeEntity entity = trustService.getTrustIncomeOrderByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 转让 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustTransEntity getTargetTransOrderByOid(String oid) {
		TrustTransEntity entity = trustService.getTrustTransOrderByOid(oid);
		
		return entity;
	}
	
	/**
	 * 获取现金管理类工具的持仓信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundForm getFundByOid(String oid) {
		FundForm form = new FundForm();
		FundEntity entity = fundService.getFundByOid(oid);
		if (null != entity) {
			try {
				BeanUtils.copyProperties(form, entity);
				CashTool cashTool = entity.getCashTool();
				CashToolRevenue revenue = cashtoolPoolService
						.findCashtoolRevenue(cashTool.getOid(), new Date(System.currentTimeMillis()));
				if (null == revenue)
					revenue = new CashToolRevenue();
				form.setCashtoolOid(cashTool.getOid());
				form.setCashtoolName(cashTool.getSecShortName());
				form.setCashtoolType(cashTool.getEtfLof());
//				form.setNetRevenue(cashTool.getDailyProfit());
//				form.setYearYield7(cashTool.getWeeklyYield());
				form.setNetRevenue(revenue.getDailyProfit());
				form.setYearYield7(revenue.getWeeklyYield());
				form.setCirculationShares(cashTool.getCirculationShares());
				form.setRiskLevel(cashTool.getRiskLevel());
				form.setDividendType(cashTool.getDividendType());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return form;
	}
	
	/**
	 * 获取现金管理类工具的订单信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundForm getFundOrderByOid(String oid) {
		FundForm form = new FundForm();
		FundOrderEntity entity = fundService.getFundOrderByOid(oid);
		if (null != entity) {
			try {
				BeanUtils.copyProperties(form, entity);
				CashTool cashTool = entity.getFundEntity().getCashTool();
				CashToolRevenue revenue = cashtoolPoolService
						.findCashtoolRevenue(cashTool.getOid(), new Date(System.currentTimeMillis()));
				if (null == revenue)
					revenue = new CashToolRevenue();
				form.setCashtoolOid(cashTool.getOid());
				form.setCashtoolName(cashTool.getSecShortName());
				form.setCashtoolType(cashTool.getEtfLof());
//				form.setNetRevenue(cashTool.getDailyProfit());
//				form.setYearYield7(cashTool.getWeeklyYield());
				form.setNetRevenue(revenue.getDailyProfit());
				form.setYearYield7(revenue.getWeeklyYield());
				form.setCirculationShares(cashTool.getCirculationShares());
				form.setRiskLevel(cashTool.getRiskLevel());
				form.setDividendType(cashTool.getDividendType());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return form;
	}
	
	/**
	 * 根据资产池id获取 预约中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<FundForm> getFundListForAppointmentByPid(String pid, Pageable pageable) {
		List<FundForm> list = Lists.newArrayList();
		
		/*Page<FundOrderEntity> entityList = fundService.findByPidForAppointment(pid, pageable);
		if (null != entityList.getContent() && entityList.getContent().size() > 0) {
			FundForm form = null;
			for (FundOrderEntity entity : entityList.getContent()) {
				form = new FundForm();
				form.setCashtoolOid(entity.getOid());
				form.setVolume(entity.getVolume());
				
				list.add(form);
			}
		}*/
		pid = assetPoolService.getPid(pid);
		List<FundOrderEntity> entityList = fundService.findByPidForAppointment(pid, pageable);
		if (null != entityList && !entityList.isEmpty()) {
			List<String> idList = Lists.newArrayList();
			for (FundOrderEntity entity : entityList) {
				idList.add(entity.getFundEntity().getCashTool().getOid());
			}
			// 获取数据采集的对象
			Map<String, CashToolRevenue> map = cashtoolPoolService
					.findCashtoolRevenue(idList, new Date(System.currentTimeMillis()));
			// 设置万分收益
			FundForm form = null;
			for (FundOrderEntity entity : entityList) {
				form = new FundForm();
				try {
					BeanUtils.copyProperties(form, entity);
					CashTool cashTool = entity.getFundEntity().getCashTool();
					CashToolRevenue revenue = new CashToolRevenue();
					if (null != map && map.containsKey(entity.getFundEntity().getCashTool().getOid()))
						revenue = map.get(entity.getFundEntity().getCashTool().getOid());
					form.setCashtoolOid(cashTool.getOid());
					form.setCashtoolName(cashTool.getSecShortName());
					form.setCashtoolType(cashTool.getEtfLof());
//					form.setNetRevenue(cashTool.getDailyProfit());
//					form.setYearYield7(cashTool.getWeeklyYield());
					form.setNetRevenue(revenue.getDailyProfit());
					form.setYearYield7(revenue.getWeeklyYield());
					form.setCirculationShares(cashTool.getCirculationShares());
					form.setRiskLevel(cashTool.getRiskLevel());
					form.setDividendType(cashTool.getDividendType());
					if ("purchase".equals(entity.getOptType())) {
						form.setVolume(entity.getVolume());
						form.setInvestDate(entity.getInvestDate());
					} else {
						form.setVolume(entity.getReturnVolume());
						form.setInvestDate(entity.getRedeemDate());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				list.add(form);
			}
		}
		
		return list;
	}
	
	/**
	 * 根据资产池id获取 成立中 货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<FundForm> getFundListByPid(String pid, Pageable pageable) {
		List<FundForm> formList = Lists.newArrayList();
		
		pid = assetPoolService.getPid(pid);
		List<FundEntity> entityList = fundService.findByPidForConfirm(pid, pageable);
		if (null != entityList && !entityList.isEmpty()) {
			List<String> idList = Lists.newArrayList();
			for (FundEntity entity : entityList) {
				idList.add(entity.getCashTool().getOid());
			}
			// 获取数据采集的对象
			Map<String, CashToolRevenue> map = cashtoolPoolService
					.findCashtoolRevenue(idList, new Date(System.currentTimeMillis()));
			// 设置万分收益
			FundForm form = null;
			for (FundEntity entity : entityList) {
				form = new FundForm();
				try {
					BeanUtils.copyProperties(form, entity);
					CashTool cashTool = entity.getCashTool();
					CashToolRevenue revenue = new CashToolRevenue();
					if (null != map && map.containsKey(entity.getCashTool().getOid()))
						revenue = map.get(entity.getCashTool().getOid());
					form.setCashtoolOid(cashTool.getOid());
					form.setCashtoolName(cashTool.getSecShortName());
					form.setCashtoolType(cashTool.getEtfLof());
//					form.setNetRevenue(cashTool.getDailyProfit());
//					form.setYearYield7(cashTool.getWeeklyYield());
					form.setNetRevenue(revenue.getDailyProfit());
					form.setYearYield7(revenue.getWeeklyYield());
					form.setCirculationShares(cashTool.getCirculationShares());
					form.setRiskLevel(cashTool.getRiskLevel());
					form.setDividendType(cashTool.getDividendType());
					form.setAmount(entity.getAmount());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formList.add(form);
			}
		}
		
		/*List<FundOrderEntity> entityList = fundService.findByPidForConfirm(pid, pageable);
		if (!entityList.isEmpty()) {
			FundForm form = null;
			for (FundOrderEntity entity : entityList) {
				form = new FundForm();
				form.setCashtoolOid(entity.getOid());
				form.setCashtoolName(entity.getName());
				form.setCashtoolType(entity.getType());
				form.setNetRevenue(entity.getNetRevenue());
				form.setYearYield7(entity.getYearYield7());
				form.setRiskLevel(entity.getRiskLevel());
				form.setDividendType(entity.getDividendType());
				form.setState(entity.getState());
				form.setOptType(entity.getType());
				if ("purchase".equals(entity.getOptType()))
					form.setVolume(entity.getVolume());
				else
					form.setVolume(entity.getReturnVolume());
				
				list.add(form);
			}
		}*/
		
		return formList;
	}
	
	/**
	 * 获取信托计划的持仓信息
	 * @param oid
	 * @param type
	 * 			类型：income（本息兑付）；transfer（转让）
	 * @return
	 */
	@Transactional
	public TrustForm getTrustByOid(String oid, String type) {
		TrustForm form = new TrustForm();
		TrustEntity entity = trustService.getTrustByOid(oid);
		if (null != entity) {
			try {
				BeanUtils.copyProperties(form, entity);
				Investment target = entity.getTarget();
				form.setTargetOid(target.getOid());
				form.setTargetName(target.getName());
				form.setTargetType(target.getType());
				form.setIncomeRate(target.getExpIncome());
				form.setExpAror(target.getExpAror());
				form.setSetDate(target.getSetDate());
				form.setArorFirstDate(target.getArorFirstDate());
				form.setAccrualDate(target.getAccrualDate());
				form.setContractDays(target.getContractDays());
				form.setLife(target.getLife());
				form.setFloorVolume(target.getFloorVolume());
				form.setCollectEndDate(target.getCollectEndDate());
				form.setCollectStartDate(target.getCollectStartDate());
				form.setCollectIncomeRate(target.getCollectIncomeRate());
				form.setExpSetDate(target.getExpSetDate());
				form.setHoldAmount(entity.getInvestAmount());
				form.setVolume(entity.getTransOutAmount());
				form.setSubjectRating(target.getSubjectRating());
				form.setRaiseScope(target.getRaiseScope());
				form.setAccrualType(target.getAccrualType());
				form.setIncome(target.getExpIncome());
				form.setState(target.getState());
				
				// 本息兑付
				if ("income".equals(type) && Investment.INVESTMENT_LIFESTATUS_STAND_UP.equals(target.getLifeState())) {
					List<TrustIncomeForm> list = targetService.getIncomeData(target, entity);
					form.setIncomeFormList(list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return form;
	}
	
	/**
	 * 获取信托计划的订单信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustForm getTrustOrderByOid(String oid, String type) {
		TrustForm form = new TrustForm();
		if ("申购".equals(type)) {
			TrustOrderEntity entity = trustService.getTrustOrderByOid(oid);
			if (null != entity) {
				try {
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setExpAror(target.getExpAror());
					form.setRaiseScope(target.getRaiseScope());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setAccrualType(target.getAccrualType());
					form.setSubjectRating(target.getSubjectRating());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setVolume(entity.getApplyVolume());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if ("本息兑付".equals(type)) {
			TrustIncomeEntity entity = trustService.getTrustIncomeOrderByOid(oid);
			if (null != entity) {
				try {
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTrustEntity().getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setExpAror(target.getExpAror());
					form.setRaiseScope(target.getRaiseScope());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setAccrualType(target.getAccrualType());
					form.setSubjectRating(target.getSubjectRating());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setVolume(entity.getIncome());
					form.setAuditVolume(entity.getAuditIncome());
					form.setInvestVolume(entity.getInvestIncome());
					form.setInvestDate(entity.getTrustEntity().getInvestDate());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			TrustTransEntity entity = trustService.getTrustTransOrderByOid(oid);
			if (null != entity) {
				try {
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTrustEntity().getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setExpAror(target.getExpAror());
					form.setRaiseScope(target.getRaiseScope());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setAccrualType(target.getAccrualType());
					form.setSubjectRating(target.getSubjectRating());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setVolume(entity.getTranVolume());
					form.setInvestDate(entity.getTrustEntity().getInvestDate());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return form;
	}
	
	/**
	 * 根据资产池id获取 预约中 的信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<TrustForm> getTrustListForAppointmentByPid(String pid) {
		List<TrustForm> formList = Lists.newArrayList();
		TrustForm form = null;
		try {
			pid = assetPoolService.getPid(pid);
			List<TrustOrderEntity> orderList = trustService.findPurchaseByPidForAppointment(pid);
			if (!orderList.isEmpty()) {
				for (TrustOrderEntity entity : orderList) {
					form = new TrustForm();
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setIncomeRate(target.getExpIncome());
					form.setExpAror(target.getExpAror());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setExpSetDate(target.getExpSetDate());
					form.setSubjectRating(target.getSubjectRating());
					form.setRaiseScope(target.getRaiseScope());
					form.setAccrualType(target.getAccrualType());
					form.setVolume(entity.getApplyVolume());
					form.setType("申购");

					formList.add(form);
				}
			}
			List<TrustIncomeEntity> incomeList = trustService.findIncomeByPidForAppointment(pid);
			if (!incomeList.isEmpty()) {
				for (TrustIncomeEntity entity : incomeList) {
					form = new TrustForm();
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTrustEntity().getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setIncomeRate(target.getExpIncome());
					form.setExpAror(target.getExpAror());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setExpSetDate(target.getExpSetDate());
					form.setSubjectRating(target.getSubjectRating());
					form.setRaiseScope(target.getRaiseScope());
					form.setAccrualType(target.getAccrualType());
					form.setInvestDate(entity.getIncomeDate());
					form.setVolume(entity.getIncome());
					form.setType("本息兑付");
					
					formList.add(form);
				}
			}
			List<TrustTransEntity> transList = trustService.findTransByPidForAppointment(pid);
			if (!transList.isEmpty()) {
				for (TrustTransEntity entity : transList) {
					form = new TrustForm();
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTrustEntity().getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setIncomeRate(target.getExpIncome());
					form.setExpAror(target.getExpAror());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setExpSetDate(target.getExpSetDate());
					form.setSubjectRating(target.getSubjectRating());
					form.setRaiseScope(target.getRaiseScope());
					form.setAccrualType(target.getAccrualType());
					form.setInvestDate(entity.getTranDate());
					form.setVolume(entity.getTranVolume());
					form.setType("转让");
					
					formList.add(form);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return formList;
	}
	
	/**
	 * 根据资产池id获取 成立中 的信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<TrustForm> getTrustListByPid(String pid, Pageable pageable) {
		List<TrustForm> formList = Lists.newArrayList();
		
		pid = assetPoolService.getPid(pid);
		List<TrustEntity> list = trustService.findByPidForConfirm(pid, pageable);
		if (null != list && !list.isEmpty()) {
			TrustForm form = null;
			for (TrustEntity entity : list) {
				form = new TrustForm();
				try {
					BeanUtils.copyProperties(form, entity);
					Investment target = entity.getTarget();
					form.setTargetOid(target.getOid());
					form.setTargetName(target.getName());
					form.setTargetType(target.getType());
					form.setIncomeRate(target.getExpIncome());
					form.setExpAror(target.getExpAror());
					form.setSetDate(target.getSetDate());
					form.setArorFirstDate(target.getArorFirstDate());
					form.setAccrualDate(target.getAccrualDate());
					form.setContractDays(target.getContractDays());
					form.setLife(target.getLife());
					form.setFloorVolume(target.getFloorVolume());
					form.setCollectEndDate(target.getCollectEndDate());
					form.setCollectStartDate(target.getCollectStartDate());
					form.setCollectIncomeRate(target.getCollectIncomeRate());
					form.setExpSetDate(target.getExpSetDate());
					form.setHoldAmount(entity.getInvestAmount());
					form.setVolume(entity.getTransOutAmount());
					form.setSubjectRating(target.getSubjectRating());
					form.setRaiseScope(target.getRaiseScope());
					form.setAccrualType(target.getAccrualType());
					form.setState(target.getLifeState());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formList.add(form);
			}
		}
		
		/*List<TrustOrderEntity> orderList = trustService.findPurchaseByPidForConfirm(pid);
		if (!orderList.isEmpty()) {
			for (TrustOrderEntity entity : orderList) {
				form = new TrustForm();
				form.setOid(entity.getOid());
				form.setTargetOid(entity.getTargetOid());
				form.setTargetName(entity.getTargetName());
				form.setTargetType(entity.getTargetType());
				form.setVolume(entity.getApplyVolume());
				form.setExpAror(entity.getIncomeRate());
				form.setSubjectRating(entity.getSubjectRating());
				form.setState(entity.getState());
				form.setType("申购");
				
				formList.add(form);
			}
		}
		List<TrustIncomeEntity> incomeList = trustService.findIncomeByPidForConfirm(pid);
		if (!incomeList.isEmpty()) {
			for (TrustIncomeEntity entity : incomeList) {
				form = new TrustForm();
				form.setOid(entity.getOid());
				form.setTargetOid(entity.getTargetOid());
				form.setTargetName(entity.getTargetName());
				form.setTargetType(entity.getTargetType());
				form.setVolume(entity.getIncome());
				form.setExpAror(entity.getIncomeRate());
				form.setSubjectRating(entity.getSubjectRating());
				form.setState(entity.getState());
				form.setType("申购");
				
				formList.add(form);
			}
		}
		List<TrustTransEntity> transList = trustService.findTransByPidForConfirm(pid);
		if (!transList.isEmpty()) {
			for (TrustTransEntity entity : transList) {
				form = new TrustForm();
				form.setOid(entity.getOid());
				form.setTargetOid(entity.getTargetOid());
				form.setTargetName(entity.getTargetName());
				form.setTargetType(entity.getTargetType());
				form.setVolume(entity.getTranVolume());
				form.setExpAror(entity.getIncomeRate());
				form.setSubjectRating(entity.getSubjectRating());
				form.setState(entity.getState());
				form.setType("申购");
				
				formList.add(form);
			}
		}*/
		
		return formList;
	}
	
	/**
	 * 逻辑删除订单
	 * @param oid
	 * @param operation
	 */
	@Transactional
	public void updateOrder(String oid, String operation) {
		if ("现金管理工具".equals(operation)) {
			fundService.updateOrder(oid);
		} else {
			trustService.updateOrder(oid, operation);
		}
	}
}
