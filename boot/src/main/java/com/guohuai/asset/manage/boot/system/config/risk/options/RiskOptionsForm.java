package com.guohuai.asset.manage.boot.system.config.risk.options;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class RiskOptionsForm {
	@NotNull
	@NotEmpty
	@NotBlank
	private String cateOid;
	private String cateTitle;

	private String indicateOid;
	private String indicateTitle;
	private String indicateDataType;
	@NotNull
	@Digits(integer = 4, fraction = 0)
	private Integer dftScore;
	private List<Option> options;

	@Data
	public static class Option {

		@NotNull
		@Digits(integer = 4, fraction = 0)
		private Integer score;
		private String param0;
		private String param1;
		private String param2;
		private String param3;

	}

}
