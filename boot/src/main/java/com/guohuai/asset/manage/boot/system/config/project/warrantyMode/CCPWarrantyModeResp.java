package com.guohuai.asset.manage.boot.system.config.project.warrantyMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CCPWarrantyModeResp {

	public CCPWarrantyModeResp(CCPWarrantyMode mode) {
		this.oid = mode.getOid();
		this.type = mode.getType();
		this.title = mode.getTitle();
		this.weight = mode.getWeight();
	}

	public CCPWarrantyModeResp(CCPWarrantyMode mode, boolean showType) {
		this(mode);
		this.showType = showType;
	}

	private String oid;
	private String type;
	private String title;
	private double weight;
	private boolean showType = false;

}
