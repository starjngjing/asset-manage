package com.guohuai.asset.manage.boot.duration.order;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.duration.capital.CapitalService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundService;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustService;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustTransEntity;
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
	
	@Autowired
	private FundService fundService;
	@Autowired
	private TrustService trustService;
	@Autowired
	private CapitalService capitalService;

	/**
	 * 货币基金（现金管理工具）申购
	 * @param from
	 * @param uid
	 */
	@Transactional
	public void purchaseForFund(FundForm form, String uid) {
		FundEntity entity = fundService.getFundByCashtoolOid(form.getCashtoolOid());
		if (null == entity) {
			entity = new FundEntity();
			entity.setOid(form.getCashtoolOid());
			entity.setCashtoolOid(form.getCashtoolOid());
			entity.setAssetPoolOid(form.getAssetPoolOid());
			entity.setAmount(BigDecimal.ZERO);
			entity.setInterestAcount(BigDecimal.ZERO);
			entity.setPurchaseVolume(form.getVolume());
			entity.setRedeemVolume(BigDecimal.ZERO);
			entity.setFrozenCapital(form.getVolume());
			entity.setOnWayCapital(BigDecimal.ZERO);
		} else {
			entity.setPurchaseVolume(entity.getPurchaseVolume().add(form.getVolume()));
			entity.setFrozenCapital(entity.getFrozenCapital().add(form.getVolume()));
		}
		entity.setState(FundEntity.INVESTING);
		
		fundService.save(entity);
		
		FundOrderEntity order = new FundOrderEntity();
		order.setOid(StringUtil.uuid());
		order.setAssetPoolCashtoolOid(form.getCashtoolOid());
		order.setName(form.getCashtoolName());
		order.setState(FundOrderEntity.STATE_AUDIT);
		order.setInvestDate(form.getInvestDate());
		order.setVolume(form.getVolume());
		order.setType(form.getCashtoolType());
		order.setOptType("purchase");
		order.setNetRevenue(form.getNetRevenue());
		order.setYearYield7(form.getYearYield7());
		order.setRiskLevel(form.getRiskLevel());
		order.setDividendType(form.getDividendType());
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
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
		FundEntity entity = fundService.getFundByCashtoolOid(form.getCashtoolOid());
		entity.setAmount(entity.getAmount().subtract(form.getVolume()));
		entity.setRedeemVolume(entity.getRedeemVolume().add(form.getVolume()));
		entity.setOnWayCapital(entity.getOnWayCapital().add(form.getVolume()));
		if ("yes".equals(form.getAllFlag()))
			entity.setState(FundEntity.INVESTEND);
		
		fundService.save(entity);
		
		FundOrderEntity order = new FundOrderEntity();
		order.setOid(StringUtil.uuid());
		order.setRedeemDate(form.getRedeemDate());
		order.setReturnVolume(form.getReturnVolume());
		order.setType("redeem");
		order.setState(FundOrderEntity.STATE_AUDIT);
		order.setAllFlag(form.getAllFlag());
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
				order.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, REDEEM, APPLY, uid, null);
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
			order.setState(FundOrderEntity.STATE_APPOINTMENT);
		else
			order.setState(FundOrderEntity.STATE_FAIL);
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);
		
		// 审核记录
		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getType()))
			entity.setAuditType(FundAuditEntity.TYPE_AUDIT + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_AUDIT + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getType())) {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
					order.getOid(), FUND, form.getAuditVolume(), order.getReturnVolume(), REDEEM, AUDIT, uid, form.getState());
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
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
			order.setState(FundOrderEntity.STATE_CONFIRM);
		else
			order.setState(FundOrderEntity.STATE_FAIL);
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);

		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getType()))
			entity.setAuditType(FundAuditEntity.TYPE_APPOINTMENT + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_APPOINTMENT + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getType())) {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
					order.getOid(), FUND, form.getReserveVolume(), order.getReturnVolume(), REDEEM, APPOINTMENT, uid, form.getState());
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
					order.getOid(), FUND, form.getReserveVolume(), order.getVolume(), PURCHASE, APPOINTMENT, uid, form.getState());
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
		if (FundAuditEntity.SUCCESSED.equals(form.getState()))
			order.setState(FundOrderEntity.STATE_SUCCESS);
		else
			order.setState(FundOrderEntity.STATE_FAIL);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(order);

		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		if ("purchase".equals(order.getType()))
			entity.setAuditType(FundAuditEntity.TYPE_CONFIRM + "-Purchase");
		else
			entity.setAuditType(FundAuditEntity.TYPE_CONFIRM + "-Redeem");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		// 资金变动记录
		if ("redeem".equals(order.getType())) {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
					order.getOid(), FUND, form.getInvestVolume(), order.getReturnVolume(), REDEEM, CONFIRM, uid, form.getState());
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getCashtoolOid(), 
					order.getOid(), FUND, form.getInvestVolume(), order.getVolume(), PURCHASE, CONFIRM, uid, form.getState());
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
		order.setTargetOid(form.getTargetOid());
		order.setAssetPoolOid(form.getAssetPoolOid());
		order.setInvestDate(form.getInvestDate());
		order.setIncomeDate(form.getIncomeDate());
		order.setApplyVolume(form.getVolume());
		order.setIncomeRate(form.getIncomeRate());
		order.setState(TrustOrderEntity.STATE_AUDIT);
		order.setAsker(uid);
		order.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustEntity trustEntity = new TrustEntity();
		trustEntity.setOid(StringUtil.uuid());
		trustEntity.setTargetOid(form.getTargetOid());
		trustEntity.setOrderOid(order.getOid());
		trustEntity.setPurchase(PURCHASE);
		trustEntity.setState(TrustOrderEntity.STATE_AUDIT);
		trustEntity.setApplyAmount(form.getVolume());
		
		trustService.save(trustEntity);
		
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
	public void auditTrust(TrustForm form, String uid) {
		// 录入申购订单的审核信息
		TrustOrderEntity order = trustService.getTrustOrderByOid(form.getOid());
		order.setAuditVolume(form.getAuditVolume());
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
			order.setState(TrustOrderEntity.STATE_APPOINTMENT);
		else
			order.setState(TrustOrderEntity.STATE_FAIL);
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
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
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
			order.setState(TrustOrderEntity.STATE_CONFIRM);
		else
			order.setState(TrustOrderEntity.STATE_FAIL);
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
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getReserveVolume(), order.getApplyVolume(), PURCHASE, APPOINTMENT, uid, form.getState());
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
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
			order.setState(TrustOrderEntity.STATE_SUCCESS);
		else
			order.setState(TrustOrderEntity.STATE_FAIL);
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
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getInvestVolume(), order.getApplyVolume(), PURCHASE, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 信托（计划）本息兑付订单
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void applyForIncome(TrustForm form, String uid) {
		TrustIncomeEntity entity = new TrustIncomeEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolTargetOid(form.getOid());
		entity.setAssetPoolTargetOid(form.getAssetPoolOid());
		entity.setSeq(1);
		entity.setState(TrustIncomeEntity.STATE_AUDIT);
		entity.setIncomeRate(form.getIncomeRate());
		entity.setIncome(form.getIncome());
		entity.setIncomeDate(form.getIncomeDate());
		// 是否兑付本金
		if (1 == form.getCapitalFlag()) {
			entity.setCapital(form.getCapital());
		}
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, INCOME, APPLY, uid, null);
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
			order.setState(TrustIncomeEntity.STATE_APPOINTMENT);
		else
			order.setState(TrustIncomeEntity.STATE_FAIL);
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getVolume(), order.getIncome(), INCOME, AUDIT, uid, form.getState());
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
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
			order.setState(TrustOrderEntity.STATE_SUCCESS);
		else
			order.setState(TrustOrderEntity.STATE_FAIL);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getVolume(), order.getAuditIncome(), INCOME, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	@Transactional
	public void transfer(TrustForm form, String uid) {
		TrustTransEntity entity = new TrustTransEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolTargetOid(form.getAssetPoolOid());
		entity.setTranVolume(form.getTranVolume());
		entity.setTranDate(form.getTranDate());
		entity.setTranCash(form.getTranCash());
		entity.setState(TrustTransEntity.STATE_AUDIT);
		entity.setCreater(uid);
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, TRANSFER, APPLY, uid, null);
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
			order.setState(TrustTransEntity.STATE_APPOINTMENT);
		else
			order.setState(TrustTransEntity.STATE_FAIL);
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getVolume(), order.getTranVolume(), TRANSFER, AUDIT, uid, form.getState());
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
		if (TrustAuditEntity.SUCCESSED.equals(form.getState()))
			order.setState(TrustTransEntity.STATE_SUCCESS);
		else
			order.setState(TrustTransEntity.STATE_FAIL);
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(order);
		
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		trustService.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				order.getOid(), TRUST, form.getVolume(), order.getTranVolume(), TRANSFER, CONFIRM, uid, form.getState());
	}
	
	/**
	 * 根据订单oid获取 申购 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundOrderEntity getFundOrderByOid(String oid) {
		FundOrderEntity entity = fundService.getFundOrderByOid(oid);
		
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
			} catch (Exception e) {
				e.printStackTrace();
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
		List<FundOrderEntity> entityList = fundService.findByPidForAppointment(pid, pageable);
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
		}
		
		return list;
	}
	
	/**
	 * 根据资产池id获取 成立中 信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<FundForm> getFundListByPid(String pid, Pageable pageable) {
		List<FundForm> list = Lists.newArrayList();
		
		List<FundOrderEntity> entityList = fundService.findByPidForConfirm(pid, pageable);
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
		}
		
		return list;
	}
	
	/**
	 * 根据资产池id获取 预约中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<TrustForm> getTrustListForAppointmentByPid(String pid) {
		List<TrustForm> list = Lists.newArrayList();
		
		return list;
	}
	
	/**
	 * 根据资产池id获取 成立中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@Transactional
	public List<TrustForm> getTrustListByPid(String pid) {
		List<TrustForm> list = Lists.newArrayList();
		
		return list;
	}
}
