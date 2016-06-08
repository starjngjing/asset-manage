package com.guohuai.asset.manage.boot.system.config.risk.warning;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskWarningForm {
	String oid;
	@NotBlank
	@NotEmpty
	@NotNull
	private String indicateOid;

	@NotNull
	@Size(max = 128, message = "")
	String title;

}
