package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.boot.investment.InvestmentMeetingAsset;
import com.guohuai.asset.manage.component.web.view.PageResp;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

//import com.guohuai.asset.hill.component.web.view.PageResp;
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class MeetingAssetListResp extends PageResp<InvestmentMeetingAsset> {

	public MeetingAssetListResp() {
		super();
	}

	public MeetingAssetListResp(Page<InvestmentMeetingAsset> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public MeetingAssetListResp(List<InvestmentMeetingAsset> approvals) {
		this(approvals, approvals.size());
	}

	public MeetingAssetListResp(List<InvestmentMeetingAsset> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}

}
