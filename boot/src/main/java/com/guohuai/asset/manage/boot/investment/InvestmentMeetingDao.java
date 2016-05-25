package com.guohuai.asset.manage.boot.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface InvestmentMeetingDao
		extends JpaRepository<InvestmentMeeting, String>, JpaSpecificationExecutor<InvestmentMeeting> {

}
