package com.guohuai.asset.manage.boot.duration.capital;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 存续期--账户对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CASH_LOG")
public class CapitalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String oid;
	// 关联资产池
	private String assetPoolOid;
	// 科目
	private String subject;
	// 关联投资标的申购订单
	private String targetOrderOid;
	// 关联投资标的本息兑付订单
	private String targetIncomeOid;
	// 关联投资标的资产转让订单
	private String targetTransOid;
	// 关联现金管理工具申赎订单
	private String cashtoolOrderOid;
	// 冻结资金入金
	private BigDecimal freezeCash;
	// 冻结资金出金
	private BigDecimal unfreezeCash;
	// 在途资金入金
	private BigDecimal transitCash;
	// 在途资金出金
	private BigDecimal untransitCash;
	// 资金变动入金
	private BigDecimal inputCash;
	// 资金变动出金
	private BigDecimal outputCash;
	// 操作人
	private String operator;
	// CreateTime
	private Timestamp createTime;
	// UpdateTime
	private Timestamp updateTime;
}
