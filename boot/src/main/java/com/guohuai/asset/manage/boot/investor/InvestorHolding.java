package com.guohuai.asset.manage.boot.investor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.order.InvestorOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 投资人 - 分仓表
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INVESTOR_HOLDING")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorHolding implements Serializable {

	private static final long serialVersionUID = 766183109221687996L;
	
	public static final String STATE_HOLDING = "HOLDING"; // 持仓中
	public static final String STATE_PARD_REDEEM = "PARD_REDEEM";//部分赎回 
	public static final String STATE_CLOSE = "CLOSE";//已平仓

	@Id
	private String oid;// 序号
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "baseAccountOid", referencedColumnName = "oid")
	private InvestorBaseAccount baseAccount;// 关联投资人-基本账户
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderOid", referencedColumnName = "oid")
	private InvestorOrder investorOrder;// 关联订单
	
	private BigDecimal balance = new BigDecimal(0);// 本金余额
	private BigDecimal compound = new BigDecimal(0);// 复利本金
	private BigDecimal uncompound = new BigDecimal(0);// 未复利本金

	private BigDecimal income = new BigDecimal(0);// 利息收益
	private BigDecimal reward = new BigDecimal(0);// 奖励收益
	private BigDecimal redeem = new BigDecimal(0);// 赎回金额
	private String state;// 类型
	private Timestamp createTime;
	private Timestamp closeTime;

}
