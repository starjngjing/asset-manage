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
 * 现金类管理工具--申购订单表
 * @author star.zhu
 * 2016年5月17日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_PURCHASE_ORDER")
public class FundPurchaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String oid;
	// 关联投资标的
	private String targetOid;
	// 关联资产池
	private String assetPoolOid;
	// 投资日
	private Date investDate; 
	// 起息日
	private Date incomeDate; 
	// 申购额度
	private BigDecimal applyVolume;
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
