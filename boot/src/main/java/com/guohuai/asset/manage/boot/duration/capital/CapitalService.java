package com.guohuai.asset.manage.boot.duration.capital;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.duration.order.OrderService;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--账户服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class CapitalService {
	
	@Autowired
	private CapitalDao capitalDao;

	/**
	 * 获取所有的出入金明细
	 * @return
	 */
	public List<CapitalForm> getAllList() {
		
		return null;
	}
	
	/**
	 * 现金管理工具的资金明细
	 * @param pid
	 * 				资产池id
	 * @param oid
	 * 				标的id
	 * @param sn
	 * 				订单id
	 * @param target
	 * 				标的类型
	 * @param capital
	 * 				操作金额
	 * @param account
	 * 				已录金额
	 * @param operation
	 * 				操作：purchase（申购）；redeem（赎回）；income（本息兑付）；transfer（资产转让）
	 * 				audit（审批）；appointment（预约）；confirm（确认）
	 * @param uid
	 * 				操作人
	 */
	public void capitalFlow(String pid, 
			String oid, 
			String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			String uid) {
		CapitalEntity entity = new CapitalEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolOid(pid);
		entity.setCashtoolOrderOid(oid);
		entity.setOperator(uid);
		if ("purchase".equals(operation)) {
			entity.setFreezeCash(capital);
			entity.setSubject("申购" + target);
			if (target.equals(OrderService.FUND))
				entity.setTargetOrderOid(sn);
			else
				entity.setCashtoolOrderOid(sn);
		} else if ("redeem".equals(operation)) {
			entity.setFreezeCash(capital);
			entity.setSubject("赎回" + target);
			if (target.equals(OrderService.FUND))
				entity.setTargetOrderOid(sn);
			else
				entity.setCashtoolOrderOid(sn);
		} else if ("income".equals(operation)) {
			entity.setTransitCash(account);
			entity.setSubject("本息兑付");
			entity.setTargetIncomeOid(sn);
		} else if ("transfer".equals(operation)) {
			entity.setTransitCash(account);
			entity.setSubject("资产转让");
			entity.setTargetTransOid(sn);
		} else if ("audit".equals(operation)) {
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("审批" + target);
		} else if ("appointment".equals(operation)) {
			entity.setUnfreezeCash(account);
			entity.setTransitCash(capital);
			entity.setSubject("预约" + target);
		} else if ("confirm".equals(operation)) {
			entity.setUntransitCash(account);
			entity.setInputCash(capital);
			entity.setSubject("确认" + target);
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
	}
	
}
