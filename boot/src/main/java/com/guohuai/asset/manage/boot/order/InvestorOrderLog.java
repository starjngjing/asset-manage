package com.guohuai.asset.manage.boot.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "T_GAM_INVESTOR_ORDER_LOG")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorOrderLog implements Serializable {
	
	private static final long serialVersionUID = 6889208404035299069L;
	
	@Id
	private String oid;//序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderOid", referencedColumnName = "oid")
	private InvestorOrder order;//关联订单	
	
	private String type;//变动类型
	private BigDecimal baseAmount = new BigDecimal(0);//原始金额	
	private BigDecimal reguAmount = new BigDecimal(0);//调整金额
	private String operator;//操作员
	private Timestamp createTime;//订单创建时间
	private Timestamp updateTime;//订单修改时间
	

}
