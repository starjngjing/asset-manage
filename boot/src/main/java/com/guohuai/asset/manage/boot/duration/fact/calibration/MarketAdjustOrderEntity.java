package com.guohuai.asset.manage.boot.duration.fact.calibration;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 市值校准关联订单
 * @author star.zhu
 * 2016年6月16日
 */
@Entity
@Data
@Table(name = "T_GAM_ASSETPOOL_MARKET_ADJUST_ORDER")
public class MarketAdjustOrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	//关联校准事件
	private String adjustOid;
	//关联订单
	private String orderOid;
}
