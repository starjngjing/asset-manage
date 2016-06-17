package com.guohuai.asset.manage.boot.investor.manage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;

import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.investor.InvestorBaseAccount;
import com.guohuai.asset.manage.boot.order.InvestorOrder;

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
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorOrderDet implements Serializable {

	private static final long serialVersionUID = 6889208404035299069L;

	private String oid;// 序号

//	private InvestorBaseAccount baseAccount;// 关联基本账户

//	private InvestorAccount account;// 关联投资人账户

	private String orderCode;// 订单号
	private String orderType;// 交易类型
	private String orderCate;// 订单类型
	private BigDecimal orderAmount = new BigDecimal(0);// 订单金额
	private Date orderDate;// 订单日期
	private BigDecimal orderVolume = new BigDecimal(0);// 订单份额
	private String orderStatus;// 订单状态
	private String entryStatus;// 订单入账状态
	private String orderStem;// 订单来源
	private BigDecimal payFee = new BigDecimal(0);// 手续费
	private String creater;// 订单创建人
	private Timestamp createTime;// 订单创建时间
	private String auditor;// 订单审核人
	private Timestamp completeTime;// 订单完成时间
	private Timestamp updateTime;// 订单修改时间

	public InvestorOrderDet(InvestorOrder io){
		if(null != io)
			BeanUtils.copyProperties(io, this);
	}
}
