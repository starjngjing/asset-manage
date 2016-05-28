package com.guohuai.asset.manage.boot.duration.order.fund;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.cashtool.CashTool;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CASHTOOL")
public class FundEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public static final String INVESTING = "0";
	public static final String INVESTEND = "-1";

	@Id
	private String oid;
	// 关联现金管理工具
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cashtoolOid", referencedColumnName = "oid")
	private CashTool cashTool;
//	private String cashtoolOid;
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
	// 状态(0：生效 ;-1：未生效 )
	private String state;
}
