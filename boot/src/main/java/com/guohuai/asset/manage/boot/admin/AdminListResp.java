package com.guohuai.asset.manage.boot.admin;

import java.util.List;

import com.guohuai.asset.manage.component.web.view.PageResp;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AdminListResp extends PageResp<AdminResp> {

	public AdminListResp(List<Admin> admins) {

		if (null != admins && admins.size() > 0) {
			super.total = admins.size();
			for (Admin a : admins) {
				super.rows.add(new AdminResp(a));
			}
		}

	}

}
