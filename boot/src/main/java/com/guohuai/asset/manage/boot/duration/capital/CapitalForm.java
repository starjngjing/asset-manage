package com.guohuai.asset.manage.boot.duration.capital;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 存续期（出入金明细）--账户对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
public class CapitalForm {

	private String oid;
	// 订单oid
	private String orderOid;
	// 科目
	private String subject;
	// 创建日期
	private Timestamp createTime;
	// 操作类型（现金管理工具申购，现金管理工赎回，信托标的申购，本息兑付，转让）
	private String operation;
	// 金额
	private BigDecimal capital;
	// 状态（未审核，资金处理中，完成）
	private String status;
	// 冻结资金入金
	private BigDecimal freezeCash;
	// 冻结资金出金
	private BigDecimal unfreezeCash;
	// 在途资金入金
	private BigDecimal transitCash;
	// 在途资金出金
	private BigDecimal untransitCash;
}
