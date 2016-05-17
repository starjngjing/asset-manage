package com.guohuai.asset.manage.boot.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentMeetingDao
		extends JpaRepository<InvestmentMeeting, String>, JpaSpecificationExecutor<InvestmentMeeting> {

}
