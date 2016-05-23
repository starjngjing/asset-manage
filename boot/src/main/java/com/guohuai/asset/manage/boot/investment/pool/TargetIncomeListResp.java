package com.guohuai.asset.manage.boot.investment.pool;

import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.component.web.view.PageResp;

//import com.guohuai.asset.hill.component.web.view.PageResp;

public class TargetIncomeListResp extends PageResp<TargetIncome> {

	public TargetIncomeListResp() {
		super();
	}

	public TargetIncomeListResp(Page<TargetIncome> page) {
		this(page.getContent(), page.getTotalElements());
	}

	public TargetIncomeListResp(List<TargetIncome> list) {
		this(list, list.size());
	}

	public TargetIncomeListResp(List<TargetIncome> list, long total) {
		this();
		super.setTotal(total);
		super.setRows(list);
	}

}
