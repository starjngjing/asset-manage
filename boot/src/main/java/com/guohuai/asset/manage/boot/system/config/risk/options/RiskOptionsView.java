package com.guohuai.asset.manage.boot.system.config.risk.options;

import lombok.Data;

@Data
public class RiskOptionsView {

	private String cateOid;
	private String cateTitle;
	private boolean showCate;

	private String indicateOid;
	private String indicateTitle;
	private boolean showIndicate;

	private String optionsOid;
	private String optionsTitle;
	private int optionsScore;
	private boolean showOptions;

}
