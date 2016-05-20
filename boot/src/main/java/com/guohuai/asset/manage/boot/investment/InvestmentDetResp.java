package com.guohuai.asset.manage.boot.investment;

import java.math.BigDecimal;

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

	public InvestmentDetResp(Investment investment) {
		super();
		if (investment.getRaiseScope() != null) {
			BigDecimal yuan = investment.getRaiseScope().divide(new BigDecimal(10000));
			investment.setRaiseScope(yuan);
		}
		this.investment = investment;
	}

	private Investment investment;
}
