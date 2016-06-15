package com.guohuai.asset.manage.boot.duration.assetPool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 存续期--资产池对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL")
public class AssetPoolEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final BigDecimal init0 = BigDecimal.ZERO;
	
	/**
	 * 每日定时任务状态(未计算，已计算，部分计算，当日不计算)
	 */
	public static final String schedule_wjs 	= "未计算";
	public static final String schedule_yjs 	= "已计算";
	public static final String schedule_bfjs 	= "部分计算";
	public static final String schedule_drbjs 	= "当日不计算";
	
	/**
	 * 当日收益分配状态(未分配，已分配)
	 */
	public static final String income_wfp 	= "未分配";
	public static final String income_yfp 	= "已分配";
	
	public AssetPoolEntity() {
		this.scale = init0;
		this.cashRate = init0;
		this.cashtoolRate = init0;
		this.targetRate = init0;
		this.cashPosition = init0;
		this.freezeCash = init0;
		this.transitCash = init0;
		this.confirmProfit = init0;
		this.factProfit = init0;
		this.scheduleState = "未计算";
		this.incomeState = "未分配";
	}

	@Id
	private String oid;
	private String name;
	// 资产规模(估值)
	private BigDecimal scale;
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
	// 成立状态(未审核,存续期,未通过,已失效)
	private String state;
	// 每日定时任务状态(未计算，已计算，部分计算，当日不计算)
	private String scheduleState;
	// 当日收益分配状态(未分配，已分配)
	private String incomeState;
	// 是否录入真实估值(0:否;1:是)
	private int factValuation;
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
	
	public static final Map<String, String> PoolState = new HashMap<>();
	static {
		PoolState.put("ASSETPOOLSTATE_01", "未审核");
		PoolState.put("ASSETPOOLSTATE_02", "存续期");
		PoolState.put("ASSETPOOLSTATE_03", "未通过");
		PoolState.put("ASSETPOOLSTATE_04", "已失效");
	}
}
