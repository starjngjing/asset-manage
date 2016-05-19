package com.guohuai.asset.manage.boot.cashtool;

import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CashToolDetResp extends BaseResp {

	public CashToolDetResp(CashTool cashTool){
		super();
		this.data = cashTool;
	}
	
	private CashTool data;
}
