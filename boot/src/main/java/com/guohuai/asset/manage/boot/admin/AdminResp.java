package com.guohuai.asset.manage.boot.admin;

import java.sql.Timestamp;

import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.NoArgsConstructor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class AdminResp extends BaseResp {

	public AdminResp(Admin a) {
		super();
		this.oid = a.getOid();
		this.publishCount = a.getPublishCount();
		this.matchingCount = a.getMatchingCount();
		this.setupCount = a.getSetupCount();
		this.updateTime = a.getUpdateTime();
		this.createTime = a.getCreateTime();
	}

	private String oid;
	private int publishCount;
	private int matchingCount;
	private int setupCount;
	private Timestamp updateTime;
	private Timestamp createTime;

}
