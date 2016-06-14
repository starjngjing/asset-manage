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
import com.guohuai.asset.manage.boot.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 投资人-基本账户
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INVESTOR_BASEACCOUNT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorBaseAccount implements Serializable {
	
	private static final long serialVersionUID = -2020103760143854837L;
	
	/**
	 * 状态status：
	 */
	public static final String STATUS_Enable = "ENABLE";//正常
	public static final String STATUS_Disable= "DISABLE";//失效
	
	@Id
	private String oid;//序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountOid", referencedColumnName = "oid")
	private InvestorAccount investorAccount;//关联持有人账户
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelOid", referencedColumnName = "oid")
	private Channel channel;//关联渠道
	
	private String status;//状态	
	private BigDecimal balance = new BigDecimal(0);//本金余额
	private BigDecimal compound = new BigDecimal(0);//复利本金
	private BigDecimal uncompound = new BigDecimal(0);//未复利本金
	private BigDecimal income = new BigDecimal(0);//利息收益
	private BigDecimal reward = new BigDecimal(0);//奖励收益	
	private BigDecimal applyInvest = new BigDecimal(0);//未确认申购
	private BigDecimal applyRedeem = new BigDecimal(0);//未确认赎回
	private Timestamp createTime;
	private Timestamp updateTime;
	
}
