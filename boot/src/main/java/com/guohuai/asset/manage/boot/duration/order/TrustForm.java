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
	private String type;
	// 投资日
	private Date investDate; 
	// 起息日
	private Date incomeDate; 
	// 申购额度
	private BigDecimal volume;
	// 收益率
	private BigDecimal incomeRate;
	
	// 转让份额
	private BigDecimal tranVolume;
	// 转让日期
	private Date tranDate; 
	// 转让溢价
	private BigDecimal tranCash; 
	// 状态
	private String state;
	
	// 兑付期数
	private int seq;
	// 实际收益
	private BigDecimal income; 
	// 是否返还本金
	private int capitalFlag;
	// 本金返还
	private BigDecimal capital;
}
