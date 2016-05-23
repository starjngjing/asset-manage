package com.guohuai.asset.manage.boot.system.config.risk.options;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.component.util.StringUtil;

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
	private int score;
	private String dft;
	private String param0;
	private String param1;
	private String param2;
	private String param3;
	private int seq;

	public String showTitle() {
		if (null == this.indicate)
			return null;
		if (this.dft.equals("YES")) {
			return "N/A";
		}
		if (this.indicate.getDataType().equals(RiskIndicate.DATA_TYPE_Number)) {
			return this.numberTitle();
		}
		if (this.indicate.getDataType().equals(RiskIndicate.DATA_TYPE_NumRange)) {
			return this.numrangeTitle();
		}
		if (this.indicate.getDataType().equals(RiskIndicate.DATA_TYPE_Text)) {
			return this.textTitle();
		}
		return null;
	}

	private String numberTitle() {
		return this.param0;
	}

	private String numrangeTitle() {
		Object[] param = new Object[4];
		param[0] = this.param0;
		param[1] = StringUtil.isEmpty(this.param1) ? "∞" : this.param1;
		param[2] = StringUtil.isEmpty(this.param2) ? "∞" : this.param2;
		param[3] = this.param3;
		return String.format("%s %s, %s %s", param);
	}

	private String textTitle() {
		return this.param0;
	}

}
