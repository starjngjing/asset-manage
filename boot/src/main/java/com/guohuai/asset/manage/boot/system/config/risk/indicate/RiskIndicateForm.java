package com.guohuai.asset.manage.boot.system.config.risk.indicate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskIndicateForm {

	private String cateOid;
	@Enumerations(values = { "WARNING", "SCORE" })
	private String cateType;
	@NotBlank
	@NotEmpty
	@NotNull
	private String cateTitle;
	private String indicateOid;
	@NotBlank
	@NotEmpty
	@NotNull
	private String indicateTitle;
	@Enumerations(values = { "NUMBER", "NUMRANGE", "TEXT" })
	private String indicateDataType;
	private String indicateDataUnit;

}
