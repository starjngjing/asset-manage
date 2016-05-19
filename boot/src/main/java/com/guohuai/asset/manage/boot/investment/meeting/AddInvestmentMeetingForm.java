package com.guohuai.asset.manage.boot.investment.meeting;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class AddInvestmentMeetingForm implements Serializable {

	private static final long serialVersionUID = -5492380071587212837L;

	private String target;
	private String participant;
	private String sn;
	private String title;
	private Timestamp conferenceTime;
}
