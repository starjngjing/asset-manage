package com.guohuai.asset.manage.boot.duration.order.target;

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
@Table(name = "T_GAM_ASSETPOOL_TARGET_INCOME")
public class TargetIncomeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	// 关联资产池投资标的
	private String assetPoolTargetOid;
	// 兑付期数
	private int seq;
	// 实际收益率
	private BigDecimal incomeRate; 
	// 实际收益
	private BigDecimal income; 
	// 本金返还
	private BigDecimal capital;
	// 收益支付日
	private Date incomeDate;
	// 状态
	private String state;
	// 申请人
	private String asker;
	// 审核人
	private String auditor; 
	// 确认人
	private String confirmer; 
	// 操作员
	private String operator;
	// UpdateTime
	private Timestamp updateTime;
	// CreateTime
	private Timestamp createTime;
}
