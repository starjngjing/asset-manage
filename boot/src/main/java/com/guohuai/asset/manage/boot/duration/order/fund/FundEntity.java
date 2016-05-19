package com.guohuai.asset.manage.boot.duration.order.fund;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CASHTOOL")
public class FundEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public static final String INVESTING = "投资中";
	public static final String INVESTEND = "投资结束";

	@Id
	private String oid;
	// 关联现金管理工具
	private String cashtoolOid;
	// 关联资产池
	private String assetPoolOid;
	// 持有份额
	private BigDecimal amount;
	// 起息份额
	private BigDecimal interestAcount;
	// 申购中份额
	private BigDecimal purchaseVolume;
	// 赎回中份额
	private BigDecimal redeemVolume;
	// 冻结资金
	private BigDecimal frozenCapital;
	// 在途资金
	private BigDecimal onWayCapital;
	// 状态(投资中 ;投资结束 )
	private String state;
}
