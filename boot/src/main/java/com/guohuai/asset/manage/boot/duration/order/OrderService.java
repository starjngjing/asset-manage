package com.guohuai.asset.manage.boot.duration.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.duration.capital.CapitalService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundOrderDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundRedeemDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundRedeemEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustAuditDao;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeDao;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustPurchaseDao;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustPurchaseEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustTransDao;
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
	private static final String AUDIT 		= "audit";
	private static final String APPOINTMENT = "appointment";
	private static final String CONFIRM 	= "confirm";
	
	@Autowired
	private FundOrderDao fundOrderDao;
	
	@Autowired
	private FundRedeemDao fundRedeemDao;
	@Autowired
	private FundAuditDao fundAuditDao;
	
	@Autowired
	private TrustPurchaseDao targetPurchaseDao;
	@Autowired
	private TrustIncomeDao targetIncomeDao;
	@Autowired
	private TrustTransDao targetTransDao;
	@Autowired
	private TrustAuditDao targetAuditDao;
	
	@Autowired
	private CapitalService capitalService;

	/**
	 * 货币基金（现金管理工具）申购
	 * @param from
	 * @param uid
	 */
	public void purchaseForFund(FundForm form, String uid) {
		FundOrderEntity entity = new FundOrderEntity();
		entity.setOid(StringUtil.uuid());
		entity.setInvestDate(form.getInvestDate());
		entity.setIncomeDate(form.getIncomeDate());
		entity.setVolume(form.getVolume());
		entity.setType("purchase");
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		FundEntity cashTool = new FundEntity();
		cashTool.setOid(StringUtil.uuid());
		cashTool.setCashtoolOid(form.getAssetPoolCashtoolOid());
		cashTool.setAssetPoolOid(form.getAssetPoolOid());
		cashTool.setPurchaseVolume(form.getVolume());
		cashTool.setFrozenCapital(form.getVolume());
		cashTool.setState(FundEntity.INVESTING);
		
		entity.setCashToolEntity(cashTool);
		
		fundOrderDao.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, PURCHASE, uid);
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 * @param uid
	 */
	public void redeem(FundForm form, String uid) {
		FundOrderEntity entity = new FundOrderEntity();
		entity.setOid(StringUtil.uuid());
		entity.setRedeemDate(form.getRedeemDate());
		entity.setBackDate(form.getBackDate());
		entity.setEndYield(form.getEndYield());
		entity.setReturnAmount(form.getReturnAmount());
		entity.setType("redeem");
		entity.setAllFlag(form.getAllFlag());
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		FundEntity cashTool = new FundEntity();
		cashTool.setOid(StringUtil.uuid());
		cashTool.setCashtoolOid(form.getAssetPoolCashtoolOid());
		cashTool.setAssetPoolOid(form.getAssetPoolOid());
		cashTool.setRedeemVolume(form.getVolume());
		cashTool.setState(FundEntity.INVESTING);
		
		entity.setCashToolEntity(cashTool);
		
		fundOrderDao.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, REDEEM, uid);
	}
	
	/**
	 * 货币基金（现金管理工具）申赎审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditFund(FundForm form, String uid) {
		// 录入申购订单的审核信息
		FundOrderEntity order = this.getFundOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setState(form.getState());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundOrderDao.save(order);
		
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
		
		fundAuditDao.save(entity);
		
		// 资金变动记录
		String operation = "申购" + FUND;
		BigDecimal capital = order.getVolume();
		if ("redeem".equals(order.getType())) {
			operation = "赎回" + FUND;
			capital = order.getReturnAmount();
		}
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), operation, form.getVolume(), capital, AUDIT, uid);
	}
	
	/**
	 * 货币基金（现金管理工具）申赎资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void appointMentForFund(FundForm form, String uid) {
		// 录入申购订单的资金预约信息
		FundOrderEntity order = this.getFundOrderByOid(form.getOid());
		order.setReserveVolume(form.getVolume());
		order.setState(form.getState());
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		// 在途资金
		if (FundOrderEntity.STATE_SUCCESS.equals(form.getState())) {
			if ("redeem".equals(order.getType()))
				order.getCashToolEntity().setOnWayCapital(form.getVolume());
		}
		
		fundOrderDao.save(order);

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
		
		fundAuditDao.save(entity);
		
		// 资金变动记录
		String operation = "申购" + FUND;
		if ("redeem".equals(order.getType())) {
			operation = "赎回" + FUND;
		}
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), operation, form.getVolume(), order.getAuditVolume(), APPOINTMENT, uid);
	}
	
	/**
	 * 货币基金（现金管理工具）申赎订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderConfirmForFund(FundForm form, String uid) {
		// 录入申购订单的确认信息
		FundOrderEntity order = this.getFundOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setState(form.getState());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		if (FundOrderEntity.STATE_SUCCESS.equals(form.getState())) {
			if ("purchase".equals(order.getType())) {
				order.getCashToolEntity().setAmount(form.getVolume());
				order.getCashToolEntity().setFrozenCapital(BigDecimal.ZERO);
			} else {
				
			}
		}
		
		fundOrderDao.save(order);

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
		
		fundAuditDao.save(entity);
		
		// 资金变动记录
		String operation = "申购" + FUND;
		if ("redeem".equals(order.getType())) {
			operation = "赎回" + FUND;
		}
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), operation, form.getVolume(), order.getReserveVolume(), CONFIRM, uid);
	}
	
	/**
	 * 信托（计划）申购
	 * @param from
	 * @param uid
	 */
	public void purchaseForTrust(TrustForm form, String uid) {
		TrustPurchaseEntity entity = new TrustPurchaseEntity();

		entity.setOid(StringUtil.uuid());
		entity.setTargetOid(form.getTargetOid());
		entity.setAssetPoolOid(form.getAssetPoolOid());
		entity.setInvestDate(form.getInvestDate());
		entity.setIncomeDate(form.getIncomeDate());
		entity.setApplyVolume(form.getVolume());
		entity.setIncomeRate(form.getIncomeRate());
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, PURCHASE, uid);
	}
	
	/**
	 * 信托（计划）申购审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditTrust(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TrustPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), order.getApplyVolume(), AUDIT, uid);
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void appointMentForTrust(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_APPOINTMENT + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的资金预约信息
		TrustPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setReserveVolume(form.getVolume());
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), order.getAuditVolume(), APPOINTMENT, uid);
	}
	
	/**
	 * 信托（计划）订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderConfirmForTrust(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Purchase");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的确认信息
		TrustPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), order.getReserveVolume(), CONFIRM, uid);
	}
/******************************************************/	
	/**
	 * 信托（计划）本息兑付订单
	 * @param form
	 * @param uid
	 */
	public void income(TrustForm form, String uid) {
		TrustIncomeEntity entity = new TrustIncomeEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolTargetOid(form.getAssetPoolOid());
		entity.setSeq(1);
		entity.setIncomeRate(form.getIncomeRate());
		entity.setIncome(form.getIncome());
		entity.setIncomeDate(form.getIncomeDate());
		// 是否兑付本金
		if (1 == form.getCapitalFlag()) {
			entity.setCapital(form.getCapital());
		}
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		targetIncomeDao.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, INCOME, uid);
	}
	
	/**
	 * 信托（计划）本息兑付审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditTrustForIncome(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TrustIncomeEntity order = this.getTargetIncomeOrderByOid(form.getOid());
		order.setAuditIncome(form.getVolume());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetIncomeDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), "本息兑付", form.getVolume(), order.getIncome(), AUDIT, uid);
	}
	
	/**
	 * 信托（计划）本息兑付确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderConfirmForIncome(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Income");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的确认信息
		TrustIncomeEntity order = this.getTargetIncomeOrderByOid(form.getOid());
		order.setInvestIncome(form.getVolume());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetIncomeDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), "本息兑付", form.getVolume(), order.getAuditIncome(), CONFIRM, uid);
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	public void transfer(TrustForm form, String uid) {
		TrustTransEntity entity = new TrustTransEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolTargetOid(form.getAssetPoolOid());
		entity.setTranVolume(form.getTranVolume());
		entity.setTranDate(form.getTranDate());
		entity.setTranCash(form.getTranCash());
		entity.setCreater(uid);
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		targetTransDao.save(entity);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), "转让", form.getTranVolume(), BigDecimal.ZERO, TRANSFER, uid);
	}
	
	/**
	 * 信托（计划）转让审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditTrustForTransfer(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_AUDIT + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TrustTransEntity order = this.getTargetTransOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetTransDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), "转让", form.getTranVolume(), order.getTranVolume(), AUDIT, uid);
	}
	
	/**
	 * 信托（计划）转让确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderConfirmForTransfer(TrustForm form, String uid) {
		TrustAuditEntity entity = new TrustAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TrustAuditEntity.TYPE_CONFIRM + "-Transfer");
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的确认信息
		TrustTransEntity order = this.getTargetTransOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetTransDao.save(order);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getTargetOid(), 
				entity.getOid(), "转让", form.getTranVolume(), order.getAuditVolume(), CONFIRM, uid);
	}
	
	/**
	 * 根据订单oid获取 申购 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	public FundOrderEntity getFundOrderByOid(String oid) {
		FundOrderEntity entity = fundOrderDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 申购 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	public FundRedeemEntity getFundRedeemOrderByOid(String oid) {
		FundRedeemEntity entity = fundRedeemDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 申购 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	public TrustPurchaseEntity getTargetPurchaseOrderByOid(String oid) {
		TrustPurchaseEntity entity = targetPurchaseDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 本息兑付 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	public TrustIncomeEntity getTargetIncomeOrderByOid(String oid) {
		TrustIncomeEntity entity = targetIncomeDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 转让 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	public TrustTransEntity getTargetTransOrderByOid(String oid) {
		TrustTransEntity entity = targetTransDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 根据资产池id获取 预约中 的信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<FundForm> getFundListForAppointmentByPid(String pid) {
		List<FundForm> list = Lists.newArrayList();
		
		List<FundOrderEntity> entityList = Lists.newArrayList();
		entityList = fundOrderDao.findByPidForAppointment(pid);
		if (!entityList.isEmpty()) {
			FundForm form = null;
			for (FundOrderEntity entity : entityList) {
				form = new FundForm();
				form.setAssetPoolCashtoolOid(entity.getOid());
				form.setVolume(entity.getVolume());
				
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
	public List<FundForm> getFundListByPid(String pid) {
		List<FundForm> list = Lists.newArrayList();
		
		return list;
	}
	
	/**
	 * 根据资产池id获取 预约中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
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
	public List<TrustForm> getTrustListByPid(String pid) {
		List<TrustForm> list = Lists.newArrayList();
		
		return list;
	}
}
