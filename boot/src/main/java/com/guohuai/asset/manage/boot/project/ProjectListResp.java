package com.guohuai.asset.manage.boot.project;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.component.web.view.PageResp;

//import com.guohuai.asset.hill.component.web.view.PageResp;

public class ProjectListResp extends PageResp<ProjectResp> {

	public ProjectListResp() {
		super();
	}

	public ProjectListResp(Page<Project> Approvals) {
		this(Approvals.getContent(), Approvals.getTotalElements());
	}

	public ProjectListResp(List<Project> approvals) {
		this(approvals, approvals.size());
	}

	public ProjectListResp(List<Project> Approvals, long total) {
		this();
		super.setTotal(total);
		for (Project approval : Approvals) {
			super.getRows().add(new ProjectResp(approval));
		}
	}

}
