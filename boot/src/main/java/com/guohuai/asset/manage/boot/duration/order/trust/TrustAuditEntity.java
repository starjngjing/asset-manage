package com.guohuai.asset.manage.boot.duration.order.trust;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 投资标的--审核记录
 * @author star.zhu
 * 2016年5月17日
 */
@Data
@Entity
@Table(name = "T_GAM_TARGET_ORDER_LOG")
public class TrustAuditEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 审核类型（审核/预约/确认)
	 */
	public static final String TYPE_AUDIT 		= "audit";
	public static final String TYPE_APPOINTMENT = "appointment";
	public static final String TYPE_CONFIRM 	= "confirm";
	
	/**
	 * 审核状态（成功/失败）
	 */
	public static final String SUCCESSED 	= "0";
	public static final String FAIL			= "-1";

	@Id
	private String oid;
	// 关联投资标的订单
	private String orderOid;
	// 审核类型
	private String auditType;
	// 审核状态
	private String auditState; 
	// 审核人
	private String auditor; 
	// 审核时间
	private Timestamp auditTime;
}
