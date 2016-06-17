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

import com.guohuai.asset.manage.boot.order.InvestorOrder;
import com.guohuai.asset.manage.component.web.view.PageResp;

public class InvestorOrderDetListResp extends PageResp<InvestorOrderDet> {

	public InvestorOrderDetListResp() {
		super();
	}

	public InvestorOrderDetListResp(Page<InvestorOrder> page) {
		this(page.getContent(), page.getTotalElements());
	}

	public InvestorOrderDetListResp(List<InvestorOrder> list) {
		this(list, null == list ? 0 : list.size());
	}

	public InvestorOrderDetListResp(List<InvestorOrder> list, long total) {
		this();
		super.setTotal(total);

		List<InvestorOrderDet> resp = new ArrayList<>();
		if (null != list)
			for (InvestorOrder ia : list) {
				resp.add(new InvestorOrderDet(ia));
			}
		super.setRows(resp);
	}

}