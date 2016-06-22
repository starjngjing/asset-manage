package com.guohuai.asset.manage.boot.duration.fact.feigin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 费金计提
 * @author star.zhu
 * 2016年6月22日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CHARGEFEE")
public class FeiginEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String oid;
	// 关联资产池
	private String assetPoolOid;
	// 费金
	private BigDecimal chargeFee;
	// 摘要
	private String digest;
	// 计提人
	private String creator;
	// 状态(-1:已删除;0:待提取;1:已提取)
	private String state;
	// 计提时间
	private Timestamp createTime;
	// 提取人
	private String drawer;
	// 提取时间
	private Timestamp drawTime;
}
