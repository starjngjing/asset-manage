package com.guohuai.asset.manage.boot.system.config.risk.options;

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
@Table(name = "T_GAM_CCR_RISK_OPTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskOptions implements Serializable {

	private static final long serialVersionUID = -2437826034228027182L;

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "indicateOid", referencedColumnName = "oid")
	private RiskIndicate indicate;
	private String title;
	private String score;
	private String dft;
	private String param0;
	private String param1;
	private String param2;
	private String param3;
	private int seq;

}
