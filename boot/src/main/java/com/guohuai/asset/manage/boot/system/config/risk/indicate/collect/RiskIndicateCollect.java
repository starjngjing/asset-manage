package com.guohuai.asset.manage.boot.system.config.risk.indicate.collect;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_CCR_RISK_INDICATE_COLLECT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskIndicateCollect implements Serializable {

	private static final long serialVersionUID = 942128317561439147L;

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "indicateOid", referencedColumnName = "oid")
	private RiskIndicate indicate;
	private String relative;
	private String collectOption;
	private String collectData;
	private int collectScore;

}
