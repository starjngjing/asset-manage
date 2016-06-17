package com.guohuai.asset.manage.boot.duration.order.trust;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.duration.capital.CapitalEntity;

@Service
public class TrustService {

	@Autowired
	private TrustDao trustDao;
	@Autowired
	private TrustOrderDao trustPurchaseDao;
	@Autowired
	private TrustIncomeDao trustIncomeDao;
	@Autowired
	private TrustTransDao trustTransDao;
	@Autowired
	private TrustAuditDao trustAuditDao;
	
	/**
	 * 录入申购订单
	 * @param entity
	 */
	@Transactional
	public void save(TrustOrderEntity entity) {
		trustPurchaseDao.save(entity);
	}
	
	/**
	 * 录入持仓信息
	 * @param entity
	 */
	@Transactional
	public void save(TrustEntity entity) {
		trustDao.save(entity);
	}
	
	@Transactional
	public void save(List<TrustEntity> list) {
		trustDao.save(list);
	}
	
	/**
	 * 录入本息兑付订单
	 * @param entity
	 */
	@Transactional
	public void save(TrustIncomeEntity entity) {
		trustIncomeDao.save(entity);
	}
	
	/**
	 * 录入转让订单
	 * @param entity
	 */
	@Transactional
	public void save(TrustTransEntity entity) {
		trustTransDao.save(entity);
	}
	
	/**
	 * 录入审核订单
	 * @param entity
	 */
	@Transactional
	public void save(TrustAuditEntity entity) {
		trustAuditDao.save(entity);
	}
	
	/**
	 * 获取持仓信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustEntity getTrustByOid(String oid) {
		TrustEntity entity = trustDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 获取持仓信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustEntity getTrustByOrderOid(String orderOid) {
		TrustEntity entity = trustDao.findByOrderOid(orderOid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 申购 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustOrderEntity getTrustOrderByOid(String oid) {
		TrustOrderEntity entity = trustPurchaseDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 本息兑付 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustIncomeEntity getTrustIncomeOrderByOid(String oid) {
		TrustIncomeEntity entity = trustIncomeDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 转让 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustTransEntity getTrustTransOrderByOid(String oid) {
		TrustTransEntity entity = trustTransDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 获取预约中的申购订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustOrderEntity> findPurchaseByPidForAppointment(String pid) {
		List<TrustOrderEntity> list = trustPurchaseDao.findPurchaseByPidForAppointment(pid);
		
		return list;
	}
	
	/**
	 * 获取预约中的本息兑付订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustIncomeEntity> findIncomeByPidForAppointment(String pid) {
		List<TrustIncomeEntity> list = trustIncomeDao.findIncomeByPidForAppointment(pid);
		
		return list;
	}
	
	/**
	 * 获取预约中的转让订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustTransEntity> findTransByPidForAppointment(String pid) {
		List<TrustTransEntity> list = trustTransDao.findTransByPidForAppointment(pid);
		
		return list;
	}
	
	/**
	 * 获取已确认的申购订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustOrderEntity> findPurchaseByPidForConfirm(String pid) {
		List<TrustOrderEntity> list = trustPurchaseDao.findPurchaseByPidForConfirm(pid);
		
		return list;
	}
	
	/**
	 * 获取已确认的本息兑付订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustIncomeEntity> findIncomeByPidForConfirm(String pid) {
		List<TrustIncomeEntity> list = trustIncomeDao.findIncomeByPidForConfirm(pid);
		
		return list;
	}
	
	/**
	 * 获取已确认的转让订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustTransEntity> findTransByPidForConfirm(String pid) {
		List<TrustTransEntity> list = trustTransDao.findTransByPidForConfirm(pid);
		
		return list;
	}
	
	/**
	 * 获取已确认订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public Object[] findByPidForConfirm(String pid, Pageable pageable) {
		Page<TrustEntity> list = trustDao.findByPidForConfirm(pid, pageable);
		if (null != list.getContent() && list.getContent().size() > 0) {
			Object[] obj = new Object[2];
			obj[0] = list.getTotalElements();
			obj[1] = list.getContent();
			return obj;
		}
		
		return null;
	}
	
	/**
	 * 获取全平台的持仓列表
	 * @return
	 */
	@Transactional
	public List<TrustEntity> findTargetList() {
		List<TrustEntity> list = trustDao.findAll();
		
		return list;
	}
	
	/**
	 * 获取资产池的持仓列表
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<TrustEntity> findTargetListByPid(String pid) {
		List<TrustEntity> list = trustDao.findFundListByPid(pid);
		
		return list;
	}
	
	/**
	 * 逻辑删除订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public void updateOrder(String oid, String operation, String operator) {
		if (CapitalEntity.APPLY.equals(operation) || CapitalEntity.TRANS.equals(operation)) {
			trustPurchaseDao.updateOrder(oid, operator);
		} else if (CapitalEntity.INCOME.equals(operation)) {
			trustIncomeDao.updateOrder(oid, operator);
		} else {
			trustTransDao.updateOrder(oid, operator);
		}
	}
	
	/**
	 * 获取本息兑付的期数
	 * @param pid
	 * @param oid
	 * @return
	 */
	public int getSeqByIncome(String oid) {
		List<TrustIncomeEntity> list = trustIncomeDao.findSeqByPidAndOidForIncome(oid);
		if (null != list && list.size() > 0) {
			return list.size();
		}
		
		return 0;
	}
}
