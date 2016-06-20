/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.investor.manage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.guohuai.asset.manage.boot.investor.InvestorHolding;
import com.guohuai.asset.manage.component.web.view.PageResp;

public class InvestorHoldingDetListResp extends PageResp<InvestorHoldingDet> {

	public InvestorHoldingDetListResp() {
		super();
	}

	public InvestorHoldingDetListResp(Page<InvestorHolding> page) {
		this(page.getContent(), page.getTotalElements());
	}

	public InvestorHoldingDetListResp(List<InvestorHolding> list) {
		this(list, null == list ? 0 : list.size());
	}

	public InvestorHoldingDetListResp(List<InvestorHolding> list, long total) {
		this();
		super.setTotal(total);

		List<InvestorHoldingDet> resp = new ArrayList<>();
		if (null != list)
			for (InvestorHolding ia : list) {
				resp.add(new InvestorHoldingDet(ia));
			}
		super.setRows(resp);
	}

}