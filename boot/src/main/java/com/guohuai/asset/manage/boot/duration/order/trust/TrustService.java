package com.guohuai.asset.manage.boot.duration.order.trust;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustOrderEntity> findPurchaseByPidForAppointment(String pid, Pageable pageable) {
		Page<TrustOrderEntity> list = trustPurchaseDao.findPurchaseByPidForAppointment(pid, pageable);
		
		return list;
	}
	
	/**
	 * 获取预约中的本息兑付订单
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustIncomeEntity> findIncomeByPidForAppointment(String pid, Pageable pageable) {
		Page<TrustIncomeEntity> list = trustIncomeDao.findIncomeByPidForAppointment(pid, pageable);
		
		return list;
	}
	
	/**
	 * 获取预约中的转让订单
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustTransEntity> findTransByPidForAppointment(String pid, Pageable pageable) {
		Page<TrustTransEntity> list = trustTransDao.findTransByPidForAppointment(pid, pageable);
		
		return list;
	}
	
	/**
	 * 获取已确认的申购订单
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustOrderEntity> findPurchaseByPidForConfirm(String pid, Pageable pageable) {
		Page<TrustOrderEntity> list = trustPurchaseDao.findPurchaseByPidForConfirm(pid, pageable);
		
		return list;
	}
	
	/**
	 * 获取已确认的本息兑付订单
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustIncomeEntity> findIncomeByPidForConfirm(String pid, Pageable pageable) {
		Page<TrustIncomeEntity> list = trustIncomeDao.findIncomeByPidForConfirm(pid, pageable);
		
		return list;
	}
	
	/**
	 * 获取已确认的转让订单
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<TrustTransEntity> findTransByPidForConfirm(String pid, Pageable pageable) {
		Page<TrustTransEntity> list = trustTransDao.findTransByPidForConfirm(pid, pageable);
		
		return list;
	}
}
