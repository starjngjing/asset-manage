package com.guohuai.asset.manage.boot.duration.assetPool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
	}

	@Id
	private String oid;
	private String name;
	// 资产规模
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
	// 状态
	private String state;
	// 创建者
	private String creater;
	// 操作员
	private String operator;
	// 创建日期
	private Timestamp createTime;
	// 更新日期
	private Timestamp updateTime;
}
