package com.guohuai.asset.manage.boot.duration.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

/**
 * 存续期--信托（计划）表单对象
 * @author star.zhu
 * 2016年5月17日
 */
@Data
public class TrustForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oid;
	// 关联投资标的
	private String targetOid;
	// 关联资产池
	private String assetPoolOid;
	// 标的名称
	private String targetName;
	// 类型
	private String targetType;
	// 成立日
	private Date setDate;
	// 拟成立日
	private Date expSetDate;
	// 投资日
	private Date investDate; 
	// 起息日
	private Date incomeDate; 
	// 申购额度
	private BigDecimal volume;
	// 收益率
	private BigDecimal incomeRate;
	// 预计年化收益	
	private BigDecimal expAror;	
	// 预计年化收益区间	
	private String expArorSec;	
	// 付息方式	
	private String accrualType;	
	// 首付息日	
	private Date arorFirstDate;	
	// 付息日	
	private Integer accrualDate;	
	// 合同年天数	
	private Integer contractDays;	
	// 标的规模	
	private BigDecimal raiseScope;	
	// 标的限期	
	private Integer life;	
	// 起购金额	
	private BigDecimal floorVolume;	
	// 持有份额	
	private BigDecimal holdAmount;	
	// 主题评级	
	private String subjectRating;	
	// 募集起始日	
	private Date collectStartDate;	
	// 募集截止日	
	private Date collectEndDate;	
	// 募集期收益	
	private BigDecimal collectIncomeRate;
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
	// 状态（审核/预约/确认 的结果 成功/失败）
	private String state;	

	// 转让份额
	private BigDecimal tranVolume;
	// 转让日期
	private Date tranDate; 
	// 转让溢价
	private BigDecimal tranCash; 
	
	// 兑付期数
	private int seq;
	// 实际收益
	private BigDecimal income; 
	// 是否返还本金
	private int capitalFlag;
	// 本金返还
	private BigDecimal capital;
	
	// 类型（申购，本息兑付，转让）
	private String type;
}
