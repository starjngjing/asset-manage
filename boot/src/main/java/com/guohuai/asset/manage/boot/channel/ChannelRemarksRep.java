package com.guohuai.asset.manage.boot.channel;

import java.sql.Timestamp;

import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
@lombok.Builder
public class ChannelRemarksRep {

	 private String remark;

	 private Timestamp time;
}
