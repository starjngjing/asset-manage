package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentMeetingUserDao
		extends JpaRepository<InvestmentMeetingUser, String>, JpaSpecificationExecutor<InvestmentMeetingUser> {

	/**
	 * 根据会议查询参会人列表
	 * 
	 * @param investmentMeeting
	 * @return
	 */
	public List<InvestmentMeetingUser> findByInvestmentMeeting(InvestmentMeeting investmentMeeting);
}
