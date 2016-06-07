package com.guohuai.asset.manage.boot.investment.meeting;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SummaryDetResp {

	private String oid;
	private String name;
	private String fkey;
	private String furl;
	private String state;
	private String operator;
	private Timestamp updateTime;
}
