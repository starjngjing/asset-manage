package com.guohuai.asset.manage.boot.approval;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.component.web.view.PageResp;

//import com.guohuai.asset.hill.component.web.view.PageResp;

public class ApprovalListResp extends PageResp<ApprovalResp> {

	public ApprovalListResp() {
		super();
	}

	public ApprovalListResp(Page<Approval> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public ApprovalListResp(List<Approval> approvals) {
		this(approvals, approvals.size());
	}

	public ApprovalListResp(List<Approval> Approvals, long total) {
		this();
		super.setTotal(total);
		for (Approval approval : Approvals) {
			super.getRows().add(new ApprovalResp(approval));
		}
	}

}
