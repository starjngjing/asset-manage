package com.guohuai.asset.manage.boot.duration.order.trust;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_TARGET")
public class TrustEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	// 关联投资标的
	private String targetOid;
	// 关联订单
	private String orderOid;
	// 关联产品
	private String productOid;
	// 申购类型(PURCHASE;TRANS_IN)
	private String purchase;
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
}
