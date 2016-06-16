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
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.guohuai.asset.manage.boot.channel.Channel;
import com.guohuai.asset.manage.boot.investor.InvestorBaseAccount;
import com.guohuai.asset.manage.boot.investor.InvestorHolding;
import com.guohuai.asset.manage.component.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestorHoldingDet {
	// 渠道信息Channel
	/**
	 * 渠道名称
	 */
	private String channelName;

	// 持仓信息
	private String oid;// 状态
	private String status;// 状态
	private BigDecimal balance = new BigDecimal(0);// 本金余额
	private BigDecimal compound = new BigDecimal(0);// 复利本金
	private BigDecimal uncompound = new BigDecimal(0);// 未复利本金

	private BigDecimal income = new BigDecimal(0);// 利息收益
	private BigDecimal reward = new BigDecimal(0);// 奖励收益
	private BigDecimal redeem = new BigDecimal(0);// 赎回金额

	private Integer holdDays; // 持仓天数
	private BigDecimal rewardRatio = new BigDecimal(0);// 奖励收益率

	public InvestorHoldingDet(InvestorHolding ih) {
		if (null == ih)
			return;
		BeanUtils.copyProperties(ih, this);

		InvestorBaseAccount iba = ih.getBaseAccount();
		if (null != iba) {
			Channel ch = iba.getChannel();
			if (null != ch)
				this.channelName = ch.getChannelName();
		}

		// 计算持仓天数
		String sta = ih.getState();
		if (StringUtils.equals(sta, InvestorHolding.STATE_CLOSE) || null != ih.getCloseTime()) {// 历史的
			holdDays = DateUtil.getDaysBetweenTwoDate(ih.getCloseTime(), ih.getCloseTime());
		} else { // 当前
			holdDays = DateUtil.getDaysBetweenTwoDate(ih.getCloseTime(), new Date());
		}

		// 计算奖励收益率
		
	}

}