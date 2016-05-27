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
 * 投资标的--申购订单表
 * @author star.zhu
 * 2016年5月17日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_TARGET_ORDER")
public class TrustOrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public static final String STATE_FAIL			= "-2";
	public static final String STATE_AUDIT			= "-1";
	public static final String STATE_APPOINTMENT	= "0";
	public static final String STATE_CONFIRM		= "1";
	public static final String STATE_SUCCESS 		= "2";
	
	@Id
	private String oid;
	// 关联投资标的
	private String targetOid;
	// 标的名称
	private String targetName;
	// 类型
	private String targetType;
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
	// 收益率
	private BigDecimal incomeRate;
	// 主题评级	
	private String subjectRating;	
	// 申请人
	private String asker;
	// 审核人
	private String auditor; 
	// 预约人
	private String reserver; 
	// 状态（-2：失败，-1：待审核，0：待预约，1：待确认，2：已成立）
	private String state;
	// 确认人
	private String confirmer; 
	// 操作员
	private String operator;
	// UpdateTime
	private Timestamp updateTime;
	// CreateTime
	private Timestamp createTime;
}
