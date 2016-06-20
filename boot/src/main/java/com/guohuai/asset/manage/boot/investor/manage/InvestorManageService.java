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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.investor.InvestorAccountDao;
import com.guohuai.asset.manage.boot.investor.InvestorBaseAccountDao;
import com.guohuai.asset.manage.boot.investor.InvestorDao;
import com.guohuai.asset.manage.boot.investor.InvestorHolding;
import com.guohuai.asset.manage.boot.investor.InvestorHoldingService;
import com.guohuai.asset.manage.boot.investor.InvestorService;
import com.guohuai.asset.manage.boot.product.Product;
import com.guohuai.asset.manage.boot.product.reward.ProductIncomeReward;
import com.guohuai.asset.manage.boot.product.reward.ProductIncomeRewardService;

@Service
@Transactional
public class InvestorManageService {
	@Autowired
	private InvestorDao investorDao;
	@Autowired
	private InvestorAccountDao investorAccountDao;
	@Autowired
	private InvestorBaseAccountDao investorBaseAccountDao;
	@Autowired
	private InvestorService InvestorService;
	@Autowired
	private InvestorHoldingService investorHoldingService;
	@Autowired
	private ProductIncomeRewardService productIncomeRewardService;

	public InvestorHoldingDetListResp holdList(Specification<InvestorHolding> spec, Pageable pageable, String accountOid) {
		InvestorHoldingDetListResp resp = new InvestorHoldingDetListResp();
		Page<InvestorHolding> pageData = investorHoldingService.list(spec, pageable);
		List<InvestorHolding> list = pageData.getContent();
		if (null != list && list.size() > 0) {
			Product prd = list.get(0).getBaseAccount().getInvestorAccount().getProduct();
			List<ProductIncomeReward> pirList = productIncomeRewardService.productRewardList(prd.getOid());

			List<InvestorHoldingDet> rows = new ArrayList<>();
			for (InvestorHolding ih : list) {
				InvestorHoldingDet ihd = new InvestorHoldingDet(ih);
				int holdDays = ihd.getHoldDays(); // 持仓天数
				ihd.setRewardRatio(getRewardRatio(pirList, holdDays));
				rows.add(ihd);
			}
			resp.setRows(rows);

		}
		resp.setTotal(pageData.getTotalElements());
		return resp;
	}

	/**
	 * 根据持仓天数计算奖励收益率
	 * 
	 * @Title: getRewardRatio
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param pirList
	 * @param holdDays
	 * @return BigDecimal 返回类型
	 */
	private BigDecimal getRewardRatio(List<ProductIncomeReward> pirList, int holdDays) {
		if (null != pirList)
			for (ProductIncomeReward pir : pirList) {
				Integer startDate = pir.getStartDate();
				Integer endDate = pir.getEndDate();
				if (null != startDate) {
					if ((null != endDate && startDate <= holdDays && holdDays <= endDate) || (null == endDate && startDate <= holdDays))
						return pir.getRatio();
				}
			}
		return new BigDecimal(0.0);
	}
}
