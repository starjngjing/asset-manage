package com.guohuai.asset.manage.boot.duration.capital.calc.error;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "T_GAM_CALCULATE_ERROR_LOG")
public class ErrorCalc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assetPoolOid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fundOid", referencedColumnName = "oid")
	private FundEntity fund;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trustOid", referencedColumnName = "oid")
	private TrustEntity trust;
	// 消息
	private JSONObject message;
	// 创建日期
	private Timestamp createTime;
	// 操作人
	private String operator;
	
}
