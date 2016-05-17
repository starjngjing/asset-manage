package com.guohuai.asset.manage.boot.duration.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundPurchaseDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundPurchaseEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundRedeemDao;
import com.guohuai.asset.manage.boot.duration.order.fund.FundRedeemEntity;
import com.guohuai.asset.manage.boot.duration.order.target.TargetAuditDao;
import com.guohuai.asset.manage.boot.duration.order.target.TargetAuditEntity;
import com.guohuai.asset.manage.boot.duration.order.target.TargetIncomeDao;
import com.guohuai.asset.manage.boot.duration.order.target.TargetIncomeEntity;
import com.guohuai.asset.manage.boot.duration.order.target.TargetPurchaseDao;
import com.guohuai.asset.manage.boot.duration.order.target.TargetPurchaseEntity;
import com.guohuai.asset.manage.boot.duration.order.target.TargetTransDao;
import com.guohuai.asset.manage.boot.duration.order.target.TargetTransEntity;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--订单服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class OrderService {
	
	@Autowired
	private FundPurchaseDao fundPurchaseDao;
	@Autowired
	private FundRedeemDao fundRedeemDao;
	@Autowired
	private FundAuditDao fundAuditDao;
	
	@Autowired
	private TargetPurchaseDao targetPurchaseDao;
	@Autowired
	private TargetIncomeDao targetIncomeDao;
	@Autowired
	private TargetTransDao targetTransDao;
	@Autowired
	private TargetAuditDao targetAuditDao;

	/**
	 * 货币基金（现金管理工具）申购
	 * @param from
	 * @param uid
	 */
	public void purchaseForFund(FundForm form, String uid) {
		FundPurchaseEntity entity = new FundPurchaseEntity();
		entity.setOid(StringUtil.uuid());
		entity.setTargetOid(form.getTargetOid());
		entity.setAssetPoolOid(form.getAssetPoolOid());
		entity.setInvestDate(form.getInvestDate());
		entity.setApplyVolume(form.getVolume());
		entity.setIncomeDate(form.getIncomeDate());
		entity.setIncomeRate(form.getIncomeRate());
		entity.setNetRevenue(form.getNetRevenue());
		entity.setYearYield7(form.getYearYield7());
		entity.setAsker(uid);
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		
		fundPurchaseDao.save(entity);
	}
	
	/**
	 * 信托（计划）申购
	 * @param from
	 * @param uid
	 */
	public void purchaseForTrust(TrustForm form, String uid) {
		TargetPurchaseEntity entity = new TargetPurchaseEntity();

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
	}
	
	/**
	 * 货币基金（现金管理工具）申购审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditFund(FundForm form, String uid) {
		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(FundAuditEntity.TYPE_AUDIT);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		FundPurchaseEntity order = this.getFundPurchaseOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundPurchaseDao.save(order);
	}
	
	/**
	 * 信托（计划）申购审核
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void auditTrust(TrustForm form, String uid) {
		TargetAuditEntity entity = new TargetAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TargetAuditEntity.TYPE_AUDIT);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TargetPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setAuditVolume(form.getVolume());
		order.setAuditor(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
	}
	
	/**
	 * 货币基金（现金管理工具）资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void cashAppointMentForFund(FundForm form, String uid) {
		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(FundAuditEntity.TYPE_APPOINTMENT);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		FundPurchaseEntity order = this.getFundPurchaseOrderByOid(form.getOid());
		order.setReserveVolume(form.getVolume());
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundPurchaseDao.save(order);
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void cashAppointMentForTrust(TrustForm form, String uid) {
		TargetAuditEntity entity = new TargetAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TargetAuditEntity.TYPE_APPOINTMENT);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TargetPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setReserveVolume(form.getVolume());
		order.setReserver(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
	}
	
	/**
	 * 货币基金（现金管理工具）订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderconfirmForFund(FundForm form, String uid) {
		FundAuditEntity entity = new FundAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(FundAuditEntity.TYPE_CONFIRM);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		fundAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		FundPurchaseEntity order = this.getFundPurchaseOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		fundPurchaseDao.save(order);
	}
	
	/**
	 * 信托（计划）订单确认
	 * @param oid
	 * 			标的oid
	 * @param uid
	 */
	public void orderconfirmForTrust(TrustForm form, String uid) {
		TargetAuditEntity entity = new TargetAuditEntity();
		entity.setOid(StringUtil.uuid());
		entity.setOrderOid(form.getOid());
		entity.setAuditType(TargetAuditEntity.TYPE_CONFIRM);
		entity.setAuditState(form.getState());
		entity.setAuditor(uid);
		entity.setAuditTime(DateUtil.getSqlCurrentDate());
		
		targetAuditDao.save(entity);
		
		// 录入申购订单的审核信息
		TargetPurchaseEntity order = this.getTargetPurchaseOrderByOid(form.getOid());
		order.setInvestVolume(form.getVolume());
		order.setConfirmer(uid);
		order.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		targetPurchaseDao.save(order);
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 * @param uid
	 */
	public void redeem(FundForm from, String uid) {
		FundRedeemEntity entity = new FundRedeemEntity();
		
		fundRedeemDao.save(entity);
	}
	
	/**
	 * 信托（计划）本息兑付订单
	 * @param form
	 * @param uid
	 */
	public void income(TrustForm form, String uid) {
		TargetIncomeEntity entity = new TargetIncomeEntity();
		
		targetIncomeDao.save(entity);
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	public void transfer(TrustForm form) {
		TargetTransEntity entity = new TargetTransEntity();
		
		targetTransDao.save(entity);
	}
	
	/**
	 * 根据订单oid获取 申购 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	public FundPurchaseEntity getFundPurchaseOrderByOid(String oid) {
		
		return null;
	}
	
	/**
	 * 根据订单oid获取 申购 的货币基金（现金管理工具）订单
	 * @param oid
	 * @return
	 */
	public TargetPurchaseEntity getTargetPurchaseOrderByOid(String oid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 预约中 的信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<FundForm> getFundListForAppointmentByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 成立中 信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<FundForm> getFundListByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 预约中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<TrustForm> getTrustListForAppointmentByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 成立中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<TrustForm> getTrustListByPid(String pid) {
		
		return null;
	}
}
