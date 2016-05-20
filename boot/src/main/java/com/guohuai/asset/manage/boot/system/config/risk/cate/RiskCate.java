package com.guohuai.asset.manage.boot.system.config.risk.cate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_CCR_RISK_CATE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskCate implements Serializable {

	private static final long serialVersionUID = 124033474932045367L;

	public static final String TYPE_Warning = "WARNING";

	public static final String TYPE_Score = "SCORE";

	@Id
	private String oid;
	private String type;
	private String title;

}
