package com.guohuai.asset.manage.boot.investment;

import java.sql.Timestamp;

import com.guohuai.asset.manage.boot.admin.AdminResp;
import com.guohuai.asset.manage.boot.admin.AdminResp.AdminRespBuilder;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.AllArgsConstructor;
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
