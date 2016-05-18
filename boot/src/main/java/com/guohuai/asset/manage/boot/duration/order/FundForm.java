package com.guohuai.asset.manage.boot.duration.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 存续期--货币基金（现金类管理工具）表单对象
 * @author star.zhu
 * 2016年5月17日
 */
@Data
public class FundForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oid;
	// 关联产品现金管理工具
	private String assetPoolCashtoolOid;
	// 关联资产池
	private String assetPoolOid;
	// 投资日
	private Date investDate; 
	// 起息日
	private Date incomeDate; 
	// 申购额度
	private BigDecimal volume;
	// 是否全部赎回
	private String allFlag;
	// 发起赎回日
	private Date redeemDate; 
	// 资金到账日
	private Date backDate; 
	// 收益截止日
	private Date endYield;
	// 赎回金额
	private BigDecimal returnAmount;
	// 年化收益率
//	private BigDecimal incomeRate;
	// 每万份收益
//	private BigDecimal netRevenue;
	// 七日年化收益
//	private BigDecimal yearYield7;
	// 申请人
	private String asker;
	// 审核人
	private String auditor; 
	// 预约人
	private String reserver; 
	// 确认人
	private String confirmer; 
	// 状态（审核/预约/确认 的结果 成功/失败）
	private String state;
	// 操作员
	private String operator;
	// UpdateTime
	private Timestamp updateTime;
	// CreateTime
	private Timestamp createTime;
}
