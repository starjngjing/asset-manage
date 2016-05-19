package com.guohuai.asset.manage.boot.duration.order.fund;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CASHTOOL_ORDER")
public class FundOrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public static final String STATE_SUCCESS 	= "0";
	public static final String STATE_FAIL		= "-1";

	@Id
	private String oid;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "assetPoolCashtoolOid", referencedColumnName = "oid")
	private FundEntity cashToolEntity;
	// 类型(purchase;redeem)
	private String type;
	// 投资日
	private Date investDate; 
	// 起息日
	private Date incomeDate; 
	// 申购额度
	private BigDecimal volume;
	// 发起赎回日
	private Date redeemDate; 
	// 资金到账日
	private Date backDate; 
	// 收益截止日
	private Date endYield;
	// 赎回金额
	private BigDecimal returnAmount;
	// 是否全部赎回
	private String allFlag;
	// 申请人
	private String asker;
	// 审核人
	private String auditor; 
	// 预约人
	private String reserver; 
	// 确认人
	private String confirmer; 
	// 审核额度
	private BigDecimal auditVolume;
	// 预约额度
	private BigDecimal reserveVolume;
	// 确认额度
	private BigDecimal investVolume;
	// 状态（-1：未审核，0：未确认，1：确认）
	private String state;
	// 操作员
	private String operator;
	// UpdateTime
	private Timestamp updateTime;
	// CreateTime
	private Timestamp createTime;
}
