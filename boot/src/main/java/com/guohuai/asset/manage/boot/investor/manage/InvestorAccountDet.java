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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.guohuai.asset.manage.boot.investor.Investor;
import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestorAccountDet {
	// 持有人信息
	private String sn;// 持有人标识
	private String phoneNum;// 手机号
	private String realName;// 真实姓名
	private String type;// 类型

	// 产品信息
	/**
	 * 产品编号
	 */
	private String code;

	/**
	 * 产品名称
	 */
	private String productName;

	// 持有人账户信息
	private String oid;// 状态
	private String status;// 状态
	private BigDecimal balance = new BigDecimal(0);// 本金余额
	private BigDecimal compound = new BigDecimal(0);// 复利本金
	private BigDecimal uncompound = new BigDecimal(0);// 未复利本金

	private BigDecimal income = new BigDecimal(0);// 利息收益
	private BigDecimal reward = new BigDecimal(0);// 奖励收益
	private BigDecimal freeze = new BigDecimal(0);// 冻结金额

	public InvestorAccountDet(InvestorAccount ia) {
		BeanUtils.copyProperties(ia, this);

		Investor i = ia.getInvestor();
		this.sn = i.getSn();
		
		this.phoneNum = i.getPhoneNum();
		if (null != phoneNum && phoneNum.length() > 8) {// 手机号码加敏处理
			this.phoneNum = StringUtils.overlay(phoneNum, "****", 3, 7);
		}
		this.realName = i.getRealName();
		this.type = i.getType();

		Product p = ia.getProduct();
		this.code = p.getCode();
		this.productName = p.getName();

	}

}