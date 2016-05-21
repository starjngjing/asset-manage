package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import com.guohuai.asset.manage.component.web.view.PageResp;


public class SummaryListResp extends  PageResp<SummaryDetResp> {

	public SummaryListResp() {
		super();
	}
	
	public SummaryListResp(List<SummaryDetResp> approvals) {
		this(approvals, approvals.size());
	}
	
	public SummaryListResp(List<SummaryDetResp> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}
}
