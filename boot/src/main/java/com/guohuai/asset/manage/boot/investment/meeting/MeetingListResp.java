package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;
import com.guohuai.asset.manage.component.web.view.PageResp;

//import com.guohuai.asset.hill.component.web.view.PageResp;

public class MeetingListResp extends PageResp<InvestmentMeeting> {

	public MeetingListResp() {
		super();
	}

	public MeetingListResp(Page<InvestmentMeeting> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public MeetingListResp(List<InvestmentMeeting> approvals) {
		this(approvals, approvals.size());
	}

	public MeetingListResp(List<InvestmentMeeting> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}

}
