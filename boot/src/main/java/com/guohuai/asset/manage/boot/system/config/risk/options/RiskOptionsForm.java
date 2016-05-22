package com.guohuai.asset.manage.boot.system.config.risk.options;

import java.util.List;

import lombok.Data;

@Data
public class RiskOptionsForm {

	private String indicateOid;
	private int dftScore;
	private List<Option> options;

	@Data
	public static class Option {

		private int score;
		private String param0;
		private String param1;
		private String param2;
		private String param3;

	}

}
