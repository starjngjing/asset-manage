package com.guohuai.asset.manage.boot.duration.assetPool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 存续期--资产池对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
public class AssetPoolForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oid;
	private String name;
	// 资产规模
	private BigDecimal scale;
	// 投资范围
	private String[] scopes;
	// 现金比例
	private BigDecimal cashRate;
	// 货币基金（现金类管理工具）比例
	private BigDecimal cashtoolRate;
	// 信托（计划）比例
	private BigDecimal targetRate;
	// 现金实际比例
	private BigDecimal cashFactRate;
	// 货币基金（现金类管理工具）实际比例
	private BigDecimal cashtoolFactRate;
	// 信托（计划）实际比例
	private BigDecimal targetFactRate;
	// 可用现金
	private BigDecimal cashPosition;
	// 冻结资金
	private BigDecimal freezeCash;
	// 在途资金
	private BigDecimal transitCash;
	// 确认收益
	private BigDecimal confirmProfit;
	// 实现收益
	private BigDecimal factProfit;
	// 偏离损益
	private BigDecimal deviationValue;
	// 每日定时任务状态(未计算，已计算，部分计算，当日不计算)
	private String scheduleState;
	// 当日收益分配状态(未分配，已分配)
	private String incomeState;
	// 是否录入真实估值(0:否;1:是)
	private int factValuation;
	// 状态
	private String state;
	// 创建者
	private String creater;
	// 操作员
	private String operator;
	// 估值基准日
	private Date baseDate;
	// 创建日期
	private Timestamp createTime;
	// 更新日期
	private Timestamp updateTime;
}
