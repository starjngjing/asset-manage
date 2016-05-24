package com.guohuai.asset.manage.boot.project;

import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResp extends BaseResp {

	private Project data;

}
