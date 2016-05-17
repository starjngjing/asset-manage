package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.component.web.view.PageResp;

//import com.guohuai.asset.hill.component.web.view.PageResp;

public class InvestmentListResp extends PageResp<Investment> {

	public InvestmentListResp() {
		super();
	}

	public InvestmentListResp(Page<Investment> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public InvestmentListResp(List<Investment> approvals) {
		this(approvals, approvals.size());
	}

	public InvestmentListResp(List<Investment> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}

}
