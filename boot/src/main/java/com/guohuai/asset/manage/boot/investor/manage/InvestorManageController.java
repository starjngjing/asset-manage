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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.investor.InvestorAccountService;
import com.guohuai.asset.manage.boot.investor.InvestorDao;
import com.guohuai.asset.manage.boot.investor.InvestorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@RequestMapping("/ams/boot/inverstorManager")
@Api("持有人名录操作相关接口")
@Slf4j
public class InvestorManageController {

	@Autowired
	private InvestorService investorService;

	@Autowired
	private InvestorManageService investorManageService;

	@Autowired
	private InvestorAccountService investorAccountService;
	@Autowired
	private InvestorDao investorDao;

	@RequestMapping(value = { "accountList" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "持有人名录账户列表")
	public @ResponseBody ResponseEntity<InvestorAccountDetListResp> accoutList(HttpServletRequest request,
			@And({ 
//				@Spec(params = "productOid", path = "product.oid", spec = Equal.class) , 
				@Spec(params = "productName", path = "product.name", spec = Like.class) 
				, @Spec(params = "investorPhoneNum", path = "investor.phoneNum", spec = Like.class)
				, @Spec(params = "investorRealName", path = "investor.realName", spec = Like.class)
				, @Spec(params = "investorType", path = "investor.type", spec = Equal.class)
				}) Specification<InvestorAccount> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "updateTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));

		Page<InvestorAccount> pageData = investorAccountService.list(spec, pageable);
		InvestorAccountDetListResp resp = new InvestorAccountDetListResp(pageData);
		return new ResponseEntity<InvestorAccountDetListResp>(resp, HttpStatus.OK);
	}

}
