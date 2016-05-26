package com.guohuai.asset.manage.boot.duration.capital;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.duration.order.OrderService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundAuditEntity;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--账户服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class CapitalService {
	
	private static final String APPLY 		= "apply";
	private static final String AUDIT 		= "audit";
	private static final String APPOINTMENT = "appointment";
	private static final String CONFIRM 	= "confirm";
	
	@Autowired
	private CapitalDao capitalDao;
	
	@Autowired
	private AssetPoolService assetPoolService;

	/**
	 * 获取所有的出入金明细
	 * @return
	 */
	@Transactional
	public List<CapitalForm> getAllList() {
		
		return null;
	}
	
	/**
	 * 资金明细
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
	 * @param operationType
	 * 				apply（申请）；audit（审批）；appointment（预约）；confirm（确认）
	 * @param uid
	 * 				操作人
	 */
	@Transactional
	public void capitalFlow1(String pid, 
			String oid, 
			String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			String operationType,
			String uid) {
		AssetPoolEntity poolEntity = assetPoolService.getByOid(pid);
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
			// 可用现金
			poolEntity.setCashPosition(poolEntity.getCashPosition().subtract(capital));
			// 冻结资金
			poolEntity.setFreezeCash(poolEntity.getFreezeCash().add(capital));
		} else if ("redeem".equals(operation)) {
			entity.setFreezeCash(capital);
			entity.setSubject("赎回" + target);
			if (target.equals(OrderService.FUND))
				entity.setTargetOrderOid(sn);
			else
				entity.setCashtoolOrderOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
		} else if ("income".equals(operation)) {
			entity.setTransitCash(capital);
			entity.setSubject("本息兑付");
			entity.setTargetIncomeOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
		} else if ("transfer".equals(operation)) {
			entity.setTransitCash(capital);
			entity.setSubject("资产转让");
			entity.setTargetTransOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
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
	
	/**
	 * 审批流
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
	 * @param operationType
	 * 				apply（申请）；audit（审批）；appointment（预约）；confirm（确认）
	 * @param uid
	 * 				操作人
	 * @param state
	 * 				操作状态：0（通过）；-1（不通过）
	 */
	@Transactional
	public void capitalFlow(String pid, 
			String oid, 
			String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			String operationType,
			String uid,
			String state) {
		AssetPoolEntity poolEntity = assetPoolService.getByOid(pid);
		CapitalEntity entity = new CapitalEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolOid(pid);
		entity.setOperator(uid);
		if (APPLY.equals(operationType)) {
			capitalWithApply(sn, target, capital, operationType, poolEntity, entity);
		} else if (AUDIT.equals(operationType)) {
			capitalWithAudit(sn, target, capital, account, operationType, poolEntity, entity, state);
		} else if (APPOINTMENT.equals(operationType)) {
			capitalWithAppointment(sn, target, capital, account, operationType, poolEntity, entity, state);
		} else if (CONFIRM.equals(operationType)) {
			capitalWithConfirm(sn, target, capital, account, operationType, poolEntity, entity, state);
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
	}
	
	/**
	 * 资金明细--申请
	 * @param sn
	 * 				订单id
	 * @param target
	 * 				标的类型
	 * @param capital
	 * 				操作金额
	 * @param operation
	 * 				操作：purchase（申购）；redeem（赎回）；income（本息兑付）；transfer（资产转让）
	 * @param poolEntity
	 * 				资产池对象
	 * @param entity
	 * 				现金变动明细
	 */
	@Transactional
	public void capitalWithApply(String sn,
			String target,
			BigDecimal capital, 
			String operation,
			AssetPoolEntity poolEntity,
			CapitalEntity entity) {
		if ("purchase".equals(operation)) {
			entity.setFreezeCash(capital);
			entity.setSubject("申购" + target);
			if (target.equals(OrderService.FUND))
				entity.setCashtoolOrderOid(sn);
			else
				entity.setTargetOrderOid(sn);
			// 可用现金
			poolEntity.setCashPosition(poolEntity.getCashPosition().subtract(capital));
			// 冻结资金
			poolEntity.setFreezeCash(poolEntity.getFreezeCash().add(capital));
		} else if ("redeem".equals(operation)) {
			entity.setTransitCash(capital);
			entity.setSubject("赎回" + target);
			entity.setCashtoolOrderOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
		} else if ("income".equals(operation)) {
			entity.setTransitCash(capital);
			entity.setSubject("本息兑付");
			entity.setTargetIncomeOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
		} else if ("transfer".equals(operation)) {
			entity.setTransitCash(capital);
			entity.setSubject("资产转让");
			entity.setTargetTransOid(sn);
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().add(capital));
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
		assetPoolService.save(poolEntity);
	}
	
	/**
	 * 资金明细--审批
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
	 * @param poolEntity
	 * 				资产池对象
	 * @param entity
	 * 				现金变动明细
	 * @param state
	 * 				操作状态：0（通过）；-1（不通过）
	 */
	@Transactional
	public void capitalWithAudit(String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			AssetPoolEntity poolEntity,
			CapitalEntity entity,
			String state) {
		if ("purchase".equals(operation)) {
			if (target.equals(OrderService.FUND)) {
				entity.setCashtoolOrderOid(sn);
			} else {
				entity.setTargetOrderOid(sn);
			}
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("审批申请中" + target + "订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(account));
				// 冻结资金
				poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 可用现金
					poolEntity.setCashPosition(poolEntity.getCashPosition().add(account).subtract(capital));
					// 冻结资金
					poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account).add(capital));
				}
			}
		} else if ("redeem".equals(operation)) {
			entity.setCashtoolOrderOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("审批赎回中" + target + "订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		} else if ("income".equals(operation)) {
			entity.setTargetIncomeOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("审批本息兑付订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		} else if ("transfer".equals(operation)) {
			entity.setTargetTransOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("审批资产转让订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
		assetPoolService.save(poolEntity);
	}
	
	/**
	 * 资金明细--预约
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
	 * @param poolEntity
	 * 				资产池对象
	 * @param entity
	 * 				现金变动明细
	 * @param state
	 * 				操作状态：0（通过）；-1（不通过）
	 */
	@Transactional
	public void capitalWithAppointment(String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			AssetPoolEntity poolEntity,
			CapitalEntity entity,
			String state) {
		if ("purchase".equals(operation)) {
			if (target.equals(OrderService.FUND)) {
				entity.setCashtoolOrderOid(sn);
			} else {
				entity.setTargetOrderOid(sn);
			}
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("预约申请中" + target + "订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(account));
				// 冻结资金
				poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 可用现金
					poolEntity.setCashPosition(poolEntity.getCashPosition().add(account).subtract(capital));
					// 冻结资金
					poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account).add(capital));
				}
			}
		} else if ("redeem".equals(operation)) {
			entity.setCashtoolOrderOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("预约赎回中" + target + "订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		} else if ("income".equals(operation)) {
			entity.setTargetIncomeOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("预约本息兑付订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		} else if ("transfer".equals(operation)) {
			entity.setTargetTransOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("预约资产转让订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 在途资金
				poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 在途资金
					poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account).add(capital));
				}
			}
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
		assetPoolService.save(poolEntity);
	}
	
	/**
	 * 资金明细--确认
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
	 * @param poolEntity
	 * 				资产池对象
	 * @param entity
	 * 				现金变动明细
	 * @param state
	 * 				操作状态：0（通过）；-1（不通过）
	 */
	@Transactional
	public void capitalWithConfirm(String sn,
			String target,
			BigDecimal capital, 
			BigDecimal account, 
			String operation,
			AssetPoolEntity poolEntity,
			CapitalEntity entity,
			String state) {
		if ("purchase".equals(operation)) {
			if (target.equals(OrderService.FUND)) {
				entity.setCashtoolOrderOid(sn);
			} else {
				entity.setTargetOrderOid(sn);
			}
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("确认申请中" + target + "订单");
			if (FundAuditEntity.FAIL.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(account));
				// 冻结资金
				poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account));
			} else {
				if (!capital.equals(account)) {
					// 可用现金
					poolEntity.setCashPosition(poolEntity.getCashPosition().add(account).subtract(capital));
				}
				// 冻结资金
				poolEntity.setFreezeCash(poolEntity.getFreezeCash().subtract(account));
			}
		} else if ("redeem".equals(operation)) {
			entity.setCashtoolOrderOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("确认赎回中" + target + "订单");
			if (FundAuditEntity.SUCCESSED.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(capital));
			}
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
		} else if ("income".equals(operation)) {
			entity.setTargetIncomeOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("确认本息兑付订单");
			if (FundAuditEntity.SUCCESSED.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(capital));
			}
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
		} else if ("transfer".equals(operation)) {
			entity.setTargetTransOid(sn);
			entity.setUnfreezeCash(account);
			entity.setFreezeCash(capital);
			entity.setSubject("确认资产转让订单");
			if (FundAuditEntity.SUCCESSED.equals(state)) {
				// 可用现金
				poolEntity.setCashPosition(poolEntity.getCashPosition().add(capital));
			}
			// 在途资金
			poolEntity.setTransitCash(poolEntity.getTransitCash().subtract(account));
		}
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		capitalDao.save(entity);
		assetPoolService.save(poolEntity);
	}
}
