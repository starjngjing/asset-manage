package com.guohuai.asset.manage.boot.duration.order.fund;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundService {

	@Autowired
	private FundDao fundDao;
	@Autowired
	private FundOrderDao fundOrderDao;
	@Autowired
	private FundRedeemDao fundRedeemDao;
	@Autowired
	private FundAuditDao fundAuditDao;
	
	/**
	 * 录入申购订单
	 * @param entity
	 */
	public void save(FundOrderEntity entity) {
		fundOrderDao.save(entity);
	}
	
	/**
	 * 录入持仓信息
	 * @param entity
	 */
	public void save(FundEntity entity) {
		fundDao.save(entity);
	}
	
	/**
	 * 录入赎回订单
	 * @param entity
	 */
	public void save(FundRedeemEntity entity) {
		fundRedeemDao.save(entity);
	}
	
	/**
	 * 录入审核订单
	 * @param entity
	 */
	public void save(FundAuditEntity entity) {
		fundAuditDao.save(entity);
	}
	
	/**
	 * 查询此现金管理类工具标的是否已持有
	 * @param cashtoolOid
	 * @return
	 */
	public FundEntity getFundByCashtoolOid(String cashtoolOid) {
		FundEntity entity = fundDao.findByCashtoolOid(cashtoolOid);
		
		return entity;
	}
	
	/**
	 * 获取持仓信息
	 * @param oid
	 * @return
	 */
	public FundEntity getFundByOid(String oid) {
		FundEntity entity = fundDao.findOne(oid);
		
		return entity;
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
	 * 根据订单oid获取 赎回 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	public FundRedeemEntity getFundRedeemOrderByOid(String oid) {
		FundRedeemEntity entity = fundRedeemDao.findByOid(oid);
		
		return entity;
	}
	
	/**
	 * 获取预约中订单
	 * @param pid
	 * @return
	 */
	public List<FundOrderEntity> findByPidForAppointment(String pid) {
		List<FundOrderEntity> list = fundOrderDao.findByPidForAppointment(pid);
		
		return list;
	}
	
	/**
	 * 获取已确认订单
	 * @param pid
	 * @return
	 */
	public List<FundOrderEntity> findByPidForConfirm(String pid) {
		List<FundOrderEntity> list = fundOrderDao.findByPidForConfirm(pid);
		
		return list;
	}
}
