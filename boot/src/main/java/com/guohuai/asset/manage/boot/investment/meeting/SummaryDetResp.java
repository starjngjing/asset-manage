package com.guohuai.asset.manage.boot.investment.meeting;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SummaryDetResp {

	private String meetingTitle;
	private String meetingSn;
	private String fkey;
	private String operator;
	private Timestamp update;
}
