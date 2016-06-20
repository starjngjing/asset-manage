package com.guohuai.asset.manage.boot.system.config.risk.warning.collect.handle;

import java.util.List;
import java.util.Map;

import com.guohuai.asset.manage.component.web.view.PageResp;

import lombok.Data;

@Data
public class RiskWarningHandleListResp extends PageResp<RiskWarningHandleDetResp> {

	public RiskWarningHandleListResp() {
		super();
	}

	public RiskWarningHandleListResp(List<RiskWarningHandleDetResp> approvals) {
		this(approvals, approvals.size());
	}

	public RiskWarningHandleListResp(List<RiskWarningHandleDetResp> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}

	private Map<String, String> investment;

}
