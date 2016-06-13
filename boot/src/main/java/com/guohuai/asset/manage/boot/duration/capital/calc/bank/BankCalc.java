package com.guohuai.asset.manage.boot.duration.capital.calc.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_CALCULATE_BANK")
public class BankCalc implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final BigDecimal init0 = BigDecimal.ZERO;
	
	public BankCalc() {
		this.capital = init0;
		this.yield = init0;
		this.profit = init0;
		this.dailyProfit = init0;
		this.totalProfit = init0;
		this.factProfit = init0;
	}
	
	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;
	// 本金
	private BigDecimal capital;
	// 收益率
	private BigDecimal yield;
	// 收益
	private BigDecimal profit;
	// 每日收益
	private BigDecimal dailyProfit;
	// 总收益
	private BigDecimal totalProfit;
	// 实际收益
	private BigDecimal factProfit;
	// 创建日期
	private Timestamp createTime;
	// 更新日期
	private Timestamp updateTime;
	// 操作人
	private String operator;
}
