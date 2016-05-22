package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import com.guohuai.asset.manage.component.web.view.PageResp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 过会标的详情列表
 * 
 * @author lirong
 *
 */

public class MeetingInvestmentListResp extends PageResp<MeetingInvestmentDetResp> {

	public MeetingInvestmentListResp() {
		super();
	}
	
	public MeetingInvestmentListResp(List<MeetingInvestmentDetResp> approvals) {
		this(approvals, approvals.size());
	}
	
	public MeetingInvestmentListResp(List<MeetingInvestmentDetResp> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}
}
