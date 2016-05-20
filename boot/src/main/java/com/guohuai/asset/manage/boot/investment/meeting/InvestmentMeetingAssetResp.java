package com.guohuai.asset.manage.boot.investment.meeting;

import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标的过会投票结果返回类
 * 
 * @author lirong
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InvestmentMeetingAssetResp {
	

	private String assetOid;

	private String assetName;

	private String meetingOid;

}
