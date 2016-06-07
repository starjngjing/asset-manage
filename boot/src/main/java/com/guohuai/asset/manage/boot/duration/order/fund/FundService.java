package com.guohuai.asset.manage.boot.duration.order.fund;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FundService {

	@Autowired
	private FundDao fundDao;
	@Autowired
	private FundOrderDao fundOrderDao;
	@Autowired
	private FundAuditDao fundAuditDao;
	
	/**
	 * 录入申购订单
	 * @param entity
	 */
	@Transactional
	public void save(FundOrderEntity entity) {
		fundOrderDao.save(entity);
	}
	
	/**
	 * 录入持仓信息
	 * @param entity
	 */
	@Transactional
	public void save(FundEntity entity) {
		fundDao.save(entity);
	}
	
	/**
	 * 录入审核订单
	 * @param entity
	 */
	@Transactional
	public void save(FundAuditEntity entity) {
		fundAuditDao.save(entity);
	}
	
	/**
	 * 查询此现金管理类工具标的是否已持有
	 * @param cashtoolOid
	 * @return
	 */
	@Transactional
	public FundEntity getFundByCashtoolOid(String oid) {
		FundEntity entity = fundDao.findByCashtoolOid(oid);
		
		return entity;
	}
	
	/**
	 * 获取持仓信息
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundEntity getFundByOid(String oid) {
		FundEntity entity = fundDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 根据订单oid获取 申购 的信托（计划）订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundOrderEntity getFundOrderByOid(String oid) {
		FundOrderEntity entity = fundOrderDao.findOne(oid);
		
		return entity;
	}
	
	/**
	 * 获取预约中订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<FundOrderEntity> findByPidForAppointment(String pid, Pageable pageable) {
		int sNo = pageable.getPageNumber();
		int eNo = pageable.getPageSize();
		List<FundOrderEntity> list = fundOrderDao.findByPidForAppointment(pid, sNo, eNo);
		
		return list;
	}
	
	/**
	 * 获取已确认订单
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<FundEntity> findByPidForConfirm(String pid, Pageable pageable) {
		Page<FundEntity> list = fundDao.findByPidForConfirm(pid, pageable);
		if (null != list.getContent() && list.getContent().size() > 0) {
			return list.getContent();
		}
		
		return null;
	}
	
	/**
	 * 获取全平台的持仓列表
	 * @return
	 */
	@Transactional
	public List<FundEntity> findFundList() {
		List<FundEntity> list = fundDao.findAll();
		
		return list;
	}
	
	/**
	 * 获取资产池的持仓列表
	 * @param pid
	 * @return
	 */
	@Transactional
	public List<FundEntity> findFundListByPid(String pid) {
		List<FundEntity> list = fundDao.findFundListByPid(pid);
		
		return list;
	}
	
	/**
	 * 逻辑删除订单
	 * @param oid
	 * @return
	 */
	@Transactional
	public void updateOrder(String oid) {
		fundOrderDao.updateOrder(oid);
	}
}
