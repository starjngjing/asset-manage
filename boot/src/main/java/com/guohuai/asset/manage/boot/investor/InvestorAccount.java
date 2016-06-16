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

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 投资人账户
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INVESTOR_ACCOUNT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorAccount implements Serializable {

	private static final long serialVersionUID = 6986152307621967799L;
	
	/**
	 * 状态status：
	 */
	public static final String STATUS_Enable = "ENABLE";//正常
	public static final String STATUS_Disable= "DISABLE";//失效
	
	
	@Id
	private String oid;//序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productOid", referencedColumnName = "oid")
	private Product product;//关联产品
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetpoolOid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;//关联资产池
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "investorOid", referencedColumnName = "oid")
	private Investor investor;//关联持有人
	
	private String status;//状态	
	private BigDecimal balance = new BigDecimal(0);//本金余额
	private BigDecimal compound = new BigDecimal(0);//复利本金
	private BigDecimal uncompound = new BigDecimal(0);//未复利本金
	
	private BigDecimal income = new BigDecimal(0);//利息收益
	private BigDecimal reward = new BigDecimal(0);//奖励收益	
	private BigDecimal freeze = new BigDecimal(0);//冻结金额	
	private Timestamp createTime;
	private Timestamp updateTime;

}
