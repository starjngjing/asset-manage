package com.guohuai.asset.manage.boot.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentMeetingVoteDao
		extends JpaRepository<InvestmentMeetingVote, String>, JpaSpecificationExecutor<InvestmentMeetingVote> {

	/**
	 * 根据会议、投资标的和投票人查询投票结果
	 * 
	 * @param meetingOid
	 * @param Investment
	 * @return
	 */
	public InvestmentMeetingVote findByInvestmentMeetingAndInvestmentAndInvestmentMeetingUser(String meetingOid,
			String Investment, String uesrOid);
}
