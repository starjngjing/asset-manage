package com.guohuai.asset.manage.boot.system.config.risk.options;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RiskOptionsCollect {

	private String cateOid;
	private String cateTitle;
	private List<Indicate> indicates = new ArrayList<Indicate>();

	@Data
	public static class Indicate {
		private String indicateOid;
		private String indicateTitle;
		private String indicateType;
		private List<Options> options = new ArrayList<Options>();

		@Data
		public static class Options {
			private String oid;
			private String param0;
			private String param1;
			private String param2;
			private String param3;
		}
	}

}
