package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentMeetingAssetDao
		extends JpaRepository<InvestmentMeetingAsset, String>, JpaSpecificationExecutor<InvestmentMeetingAsset> {

	public List<InvestmentMeetingAsset> findByInvestmentMeeting(InvestmentMeeting investmentMeeting);
}
