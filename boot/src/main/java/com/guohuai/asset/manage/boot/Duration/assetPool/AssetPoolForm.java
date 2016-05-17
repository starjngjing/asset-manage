package com.guohuai.asset.manage.boot.Duration.assetPool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Id;

import lombok.Data;

/**
 * 存续期--资产池对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
public class AssetPoolForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private String type;
	// 成立日
	private Date setUpDate;
	// 资产规模
	private BigDecimal scale;
	// 现金比例
	private BigDecimal proportion_cash;
	// 货币基金（现金类管理工具）比例
	private BigDecimal proportion_fund;
	// 信托（计划）比例
	private BigDecimal proportion_trust;
	// 可用现金
	private BigDecimal cash;
	// 冻结资金
	private BigDecimal frozen_capital;
	// 在途资金
	private BigDecimal transit_funds;
}
