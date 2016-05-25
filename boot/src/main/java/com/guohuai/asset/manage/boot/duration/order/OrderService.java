package com.guohuai.asset.manage.boot.duration.order;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.duration.capital.CapitalService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundOrderEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundService;
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
	
	private static final String APPLY 		= "apply";
	private static final String AUDIT 		= "audit";
	private static final String APPOINTMENT = "appointment";
	private static final String CONFIRM 	= "confirm";
	
	@Autowired
	private TrustPurchaseDao targetPurchaseDao;
	@Autowired
	private TrustIncomeDao targetIncomeDao;
	@Autowired
	private TrustTransDao targetTransDao;
	@Autowired
	private TrustAuditDao targetAuditDao;
	
	@Autowired
	private FundService fundService;
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
		entity.setVolume(form.getVolume());
		entity.setType("purchase");
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
	
		FundEntity cashTool = fundService.getFundByCashtoolOid(form.getAssetPoolCashtoolOid());
		if (null == cashTool) {
			cashTool = new FundEntity();
			cashTool.setOid(StringUtil.uuid());
			cashTool.setCashtoolOid(form.getAssetPoolCashtoolOid());
			cashTool.setAssetPoolOid(form.getAssetPoolOid());
		}
		cashTool.setPurchaseVolume(cashTool.getPurchaseVolume().add(form.getVolume()));
		cashTool.setFrozenCapital(cashTool.getFrozenCapital().add(form.getVolume()));
		cashTool.setState(FundEntity.INVESTING);
		
		fundService.save(cashTool);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, PURCHASE, APPLY, uid);
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
		entity.setReturnVolume(form.getReturnVolume());
		entity.setType("redeem");
		entity.setAllFlag(form.getAllFlag());
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundService.save(entity);
		
		FundEntity cashTool = fundService.getFundByCashtoolOid(form.getAssetPoolCashtoolOid());
		cashTool.setAmount(cashTool.getAmount().subtract(form.getVolume()));
		cashTool.setRedeemVolume(cashTool.getRedeemVolume().add(form.getVolume()));
		cashTool.setOnWayCapital(cashTool.getOnWayCapital().add(form.getVolume()));
		if ("yes".equals(form.getAllFlag()))
			cashTool.setState(FundEntity.INVESTEND);
		
		fundService.save(cashTool);
		
		// 资金变动记录
		capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
				entity.getOid(), FUND, form.getVolume(), BigDecimal.ZERO, REDEEM, APPLY, uid);
	}
	
	/**
	 * 货币基金（现金管理工具）申赎审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditForFund(FundForm form, String uid) {
		// 录入申购订单的审核信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setState(form.getState());
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
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getReturnVolume(), REDEEM, AUDIT, uid);
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getVolume(), PURCHASE, AUDIT, uid);
		}
	}
	
	/**
	 * 货币基金（现金管理工具）申赎资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void appointmentForFund(FundForm form, String uid) {
		// 录入申购订单的资金预约信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setReserveVolume(form.getVolume());
		order.setState(form.getState());
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
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getReturnVolume(), REDEEM, APPOINTMENT, uid);
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getVolume(), PURCHASE, APPOINTMENT, uid);
		}
	}
	
	/**
	 * 货币基金（现金管理工具）申赎订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderConfirmForFund(FundForm form, String uid) {
		// 录入申购订单的确认信息
		FundOrderEntity order = fundService.getFundOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setState(form.getState());
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
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getReturnVolume(), REDEEM, CONFIRM, uid);
		} else {
			capitalService.capitalFlow(form.getAssetPoolOid(), form.getAssetPoolCashtoolOid(), 
					entity.getOid(), FUND, form.getVolume(), order.getVolume(), PURCHASE, CONFIRM, uid);
		}
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
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, PURCHASE, APPLY, uid);
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
				entity.getOid(), TRUST, form.getVolume(), order.getApplyVolume(), PURCHASE, AUDIT, uid);
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void appointmentForTrust(TrustForm form, String uid) {
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
				entity.getOid(), TRUST, form.getVolume(), order.getApplyVolume(), PURCHASE, APPOINTMENT, uid);
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
				entity.getOid(), TRUST, form.getVolume(), order.getApplyVolume(), PURCHASE, CONFIRM, uid);
	}
	
	/**
	 * 信托（计划）本息兑付订单
	 * @param form
	 * @param uid
	 */
	public void applyForIncome(TrustForm form, String uid) {
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
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, INCOME, APPLY, uid);
	}
	
	/**
	 * 信托（计划）本息兑付审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditForIncome(TrustForm form, String uid) {
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
				entity.getOid(), TRUST, form.getVolume(), order.getIncome(), INCOME, AUDIT, uid);
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
				entity.getOid(), TRUST, form.getVolume(), order.getAuditIncome(), INCOME, CONFIRM, uid);
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
				entity.getOid(), TRUST, form.getVolume(), BigDecimal.ZERO, TRANSFER, APPLY, uid);
	}
	
	/**
	 * 信托（计划）转让审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditForTransfer(TrustForm form, String uid) {
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
				entity.getOid(), TRUST, form.getVolume(), order.getTranVolume(), TRANSFER, AUDIT, uid);
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
				entity.getOid(), TRUST, form.getVolume(), order.getTranVolume(), TRANSFER, CONFIRM, uid);
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
	 * 获取现金管理类工具的持仓信息
	 * @param oid
	 * @return
	 */
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
	public List<FundForm> getFundListForAppointmentByPid(String pid) {
		List<FundForm> list = Lists.newArrayList();
		
		List<FundOrderEntity> entityList = Lists.newArrayList();
		entityList = fundService.findByPidForAppointment(pid);
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
