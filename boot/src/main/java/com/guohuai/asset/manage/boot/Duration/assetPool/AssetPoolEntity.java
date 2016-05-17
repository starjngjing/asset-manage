package com.guohuai.asset.manage.boot.Duration.assetPool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 存续期--资产池对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL")
public class AssetPoolEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String oid;
	private String name;
	private String type;
	// 资产规模
	private BigDecimal scale;
	// 现金比例
	private BigDecimal cashRate;
	// 货币基金（现金类管理工具）比例
	private BigDecimal cashtoolRate;
	// 信托（计划）比例
	private BigDecimal targetRate;
	// 可用现金
	private BigDecimal cashPosition;
	// 冻结资金
	private BigDecimal freezeCash;
	// 在途资金
	private BigDecimal transitCash;
	// 状态
	private String state;
	// 创建者
	private String creater;
	// 操作员
	private String operator;
	// 创建日期
	private Timestamp createTime;
	// 更新日期
	private Timestamp updateTime;
}