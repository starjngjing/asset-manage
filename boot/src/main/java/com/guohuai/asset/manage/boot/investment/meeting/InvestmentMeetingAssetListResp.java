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
public class InvestmentMeetingAssetListResp extends PageResp<InvestmentMeetingAsset> {

	public InvestmentMeetingAssetListResp() {
		super();
	}

	public InvestmentMeetingAssetListResp(Page<InvestmentMeetingAsset> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public InvestmentMeetingAssetListResp(List<InvestmentMeetingAsset> approvals) {
		this(approvals, approvals.size());
	}

	public InvestmentMeetingAssetListResp(List<InvestmentMeetingAsset> Approvals, long total) {
		this();
		super.setTotal(total);
		super.setRows(Approvals);
	}

}
