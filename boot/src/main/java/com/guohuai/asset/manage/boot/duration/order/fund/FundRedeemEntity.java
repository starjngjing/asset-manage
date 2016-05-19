package com.guohuai.asset.manage.boot.duration.order.fund;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 投资标的--本息兑付订单
 * @author star.zhu
 * 2016年5月17日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_REDEEM_ORDER")
public class FundRedeemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	// 关联投资标的
	private String targetOid;
	// 关联资产池
	private String assetPoolOid;
	// 发起赎回日
	private Date redeemDate; 
	// 资金到账日
	private Date backDate; 
	// 收益截止日
	private Date endYield;
	// 赎回金额
	private BigDecimal return_amount;
	// 审核额度
	private BigDecimal auditVolume;
	// 预约额度
	private BigDecimal reserveVolume;
	// 确认额度
	private BigDecimal investVolume;
	// 年化收益率
	private BigDecimal incomeRate;
	// 每万份收益
	private BigDecimal netRevenue;
	// 七日年化收益
	private BigDecimal yearYield7;
	// 申请人
	private String asker;
	// 审核人
	private String auditor; 
	// 预约人
	private String reserver; 
	// 确认人
	private String confirmer; 
	// 操作员
	private String operator;
	// UpdateTime
	private Timestamp updateTime;
	// CreateTime
	private Timestamp createTime;
}
