package com.guohuai.asset.manage.boot.duration.fact.calibration;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.order.InvestorOrder;

import lombok.Data;

/**
 * 市值校准关联订单
 * @author star.zhu
 * 2016年6月16日
 */
@Entity
@Data
@Table(name = "T_GAM_ASSETPOOL_MARKET_ADJUST_ORDER")
public class MarketAdjustOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	//关联校准事件
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adjustOid", referencedColumnName = "oid")
	private MarketAdjust marketAdjust;
	//关联订单
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderOid", referencedColumnName = "oid")
	private InvestorOrder investorOrder;
}
