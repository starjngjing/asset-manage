package com.guohuai.asset.manage.boot.duration.capital.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;

import lombok.Data;

/**
 * 资产池每日收益
 * @author star.zhu
 * 2016年6月13日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_INCOME_DETAIL")
public class AssetPoolCalc implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final BigDecimal init0 = BigDecimal.ZERO;
	
	public AssetPoolCalc() {
		this.capital = init0;
		this.yield = init0;
		this.profit = init0;
	}
	
	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assetpoolOid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;
	// 估值
	private BigDecimal capital;
	// 收益率
	private BigDecimal yield;
	// 收益
	private BigDecimal profit;
	// 收益基准日
	private Date baseDate;
	// 创建日期
	private Timestamp createTime;
	// 更新日期
	private Timestamp updateTime;
	// 触发方式
	private EventType eventType;
	
	public enum EventType {
		SCHEDULE,	// 定时触发
		USER_CALC,	// 手动触发 - 有多少算多少 
		USER_NONE;	// 手动触发 - 不计算
	}
}
