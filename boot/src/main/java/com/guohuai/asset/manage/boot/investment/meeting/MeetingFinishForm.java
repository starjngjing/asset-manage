package com.guohuai.asset.manage.boot.investment.meeting;

import java.io.Serializable;

import lombok.Data;

@Data
public class MeetingFinishForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7461942881335219977L;

	private String oid;

	private String targets;

	private String meetingRemark;
}
