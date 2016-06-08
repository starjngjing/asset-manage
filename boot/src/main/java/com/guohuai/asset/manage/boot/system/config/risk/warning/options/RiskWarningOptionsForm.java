package com.guohuai.asset.manage.boot.system.config.risk.warning.options;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class RiskWarningOptionsForm {
	@NotNull
	@NotEmpty
	@NotBlank
	private String cateOid;
	private String cateTitle;
	
	private String indicateOid;
	@NotNull
	@NotEmpty
	@NotBlank
	private String warningOid;
	private String warningTitle;
	@NotNull
	@NotEmpty
	@NotBlank
	@Digits(integer = 4, fraction = 0)
	private List<Option> options;

	@Data
	public static class Option {

		@NotNull
		@NotBlank
		@NotEmpty
		@Digits(integer = 4, fraction = 0)
		private String wlevel;
		private String param0;
		private String param1;
		private String param2;
		private String param3;

	}

}
