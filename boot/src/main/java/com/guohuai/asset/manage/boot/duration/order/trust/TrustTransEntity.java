package com.guohuai.asset.manage.boot.duration.order.trust;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 投资标的--资产转让订单
 * @author star.zhu
 * 2016年5月17日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_TARGET_TRANS")
public class TrustTransEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	// 关联资产池投资标的
	private String assetPoolTargetOid;
	// 转让份额
	private BigDecimal tranVolume;
	// 审核额度
	private BigDecimal auditVolume;
	// 确认额度
	private BigDecimal investVolume;
	// 转让日期
	private Date tranDate; 
	// 转让溢价
	private BigDecimal tranCash; 
	// 转让操作员
	private String creater;
	// 状态（-1：未审核，0：未确认，1：确认）
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
