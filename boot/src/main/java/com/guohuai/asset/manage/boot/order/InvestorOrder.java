package com.guohuai.asset.manage.boot.order;

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

import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.investor.InvestorBaseAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 投资人-交易委托单
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INVESTOR_ORDER")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorOrder implements Serializable {
	
	private static final long serialVersionUID = 6889208404035299069L;
	
	/**
	 * 交易类型orderType：
	 */
	public static final String ORDER_TYPE_Invest = "INVEST";//申购
	public static final String ORDER_TYPE_PartRedeem = "PART_REDEEM";//部分赎回
	public static final String ORDER_TYPE_FullRedeem = "FULL_REDEEM";//全部赎回
	public static final String ORDER_TYPE_BuyIn = "BUY_IN";//买入
	public static final String ORDER_TYPE_PartSellOut = "PART_SELL_OUT";//部分卖出
	public static final String ORDER_TYPE_FullSellOut = "FULL_SELL_OUT";//全部卖出
	
	/**
	 * 订单状态orderStatus：
	 */
	public static final String STATUS_Submit = "SUBMIT";//未确认
	public static final String STATUS_Confirm = "CONFIRM";//确认
	public static final String STATUS_Disable = "DISABLE";//失效
	public static final String STATUS_Calcing= "CALCING";//清算中
	
	/**
	 * 订单来源orderStem：
	 */
	public static final String ORDER_STEM_User = "USER";//用户申请
	public static final String ORDER_STEM_Plateform = "PLATFORM";//平台补录
	
	@Id
	private String oid;//序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "baseAccountOid", referencedColumnName = "oid")
	private InvestorBaseAccount baseAccount;//关联基本账户

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountOid", referencedColumnName = "oid")
	private InvestorAccount account;//关联投资人账户
	
	private String orderCode;//订单号
	private String orderType;//交易类型
	private BigDecimal orderAmount = new BigDecimal(0);//订单金额	
	private BigDecimal orderVolume = new BigDecimal(0);//订单份额
	private String orderStatus;//订单状态
	private String orderStem;//订单来源	
	private BigDecimal payFee = new BigDecimal(0);//手续费
	private String creater;//订单创建人
	private Timestamp createTime;//订单创建时间
	private String auditor;//订单审核人
	private Timestamp completeTime;//订单完成时间
	private Timestamp updateTime;//订单修改时间
	private Date orderDate;//订单日期
	

}
