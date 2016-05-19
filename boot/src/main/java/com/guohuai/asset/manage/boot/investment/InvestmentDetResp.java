package com.guohuai.asset.manage.boot.investment;

import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentDetResp extends BaseResp {

	public InvestmentDetResp(Investment investment){
		super();
		this.investment = investment;
	}
	
	private Investment investment;
}
