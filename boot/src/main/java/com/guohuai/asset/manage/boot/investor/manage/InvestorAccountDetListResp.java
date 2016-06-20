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

import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.component.web.view.PageResp;

public class InvestorAccountDetListResp extends PageResp<InvestorAccountDet> {

	public InvestorAccountDetListResp() {
		super();
	}

	public InvestorAccountDetListResp(Page<InvestorAccount> page) {
		this(page.getContent(), page.getTotalElements());
	}

	public InvestorAccountDetListResp(List<InvestorAccount> list) {
		this(list, null == list ? 0 : list.size());
	}

	public InvestorAccountDetListResp(List<InvestorAccount> list, long total) {
		this();
		super.setTotal(total);

		List<InvestorAccountDet> resp = new ArrayList<>();
		if (null != list)
			for (InvestorAccount ia : list) {
				resp.add(new InvestorAccountDet(ia));
			}
		super.setRows(resp);
	}

}