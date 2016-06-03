package com.guohuai.asset.manage.boot.duration.order.trust;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.investment.Investment;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_TARGET")
public class TrustEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final BigDecimal init0 = BigDecimal.ZERO;
	
	/**
	 * 状态
	 */
	public static final String INVESTING = "0";
	public static final String INVESTEND = "-1";
	
	public TrustEntity() {
		this.confirmAmount = init0;
		this.investAmount = init0;
		this.transOutAmount = init0;
		this.transOutFee = init0;
		this.transInAmount = init0;
		this.transInFee = init0;
		this.dailyProfit = init0;
		this.totalProfit = init0;
	}

	@Id
	private String oid;
	// 关联投资标的
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "targetOid", referencedColumnName = "oid")
	private Investment target;
//	private String targetOid;
	// 关联订单
	private String orderOid;
	// 关联产品
	private String assetPoolOid;
	// 收益分配方式
	private String returnsType;
	// 申购类型(PURCHASE;TRANS_IN)
	private String purchase;
	// 投资日
	private Date investDate; 
	// 起息日
//	private Date incomeDate; 
	// 状态(申请中;审核成功;审核失败;预约中;预约失败;投资成功;已转出;投资结束)
	private String state;
	// 申请份额
	private BigDecimal applyAmount;
	// 批准份额
	private BigDecimal confirmAmount;
	// 投资(持有)份额
	private BigDecimal investAmount;
	// 转出份额
	private BigDecimal transOutAmount;
	// 转出费用
	private BigDecimal transOutFee;
	// 转入份额
	private BigDecimal transInAmount;
	// 转入费用
	private BigDecimal transInFee;
	// 收益方式（amortized_cost：摊余成本法；book_value：账面价值法）
	private String profitType;
	// 每日收益
	private BigDecimal dailyProfit;
	// 累计收益
	private BigDecimal totalProfit;
}
