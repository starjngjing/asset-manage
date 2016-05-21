package com.guohuai.asset.manage.boot.investment.meeting;

import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class MeetingDetResp extends BaseResp {

	public MeetingDetResp(InvestmentMeeting meeting) {
		this.data = meeting;
	}
	private InvestmentMeeting data;
}
