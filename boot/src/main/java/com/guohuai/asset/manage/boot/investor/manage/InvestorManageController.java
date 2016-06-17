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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.investor.InvestorAccountService;
import com.guohuai.asset.manage.boot.investor.InvestorDao;
import com.guohuai.asset.manage.boot.investor.InvestorHolding;
import com.guohuai.asset.manage.boot.investor.InvestorHoldingService;
import com.guohuai.asset.manage.boot.investor.InvestorService;
import com.guohuai.asset.manage.boot.order.InvestorOrder;
import com.guohuai.asset.manage.boot.order.InvestorOrderService;
import com.guohuai.asset.manage.component.exception.AMPException;

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
	private InvestorOrderService investorOrderService;

	@Autowired
	private InvestorHoldingService investorHoldingService;
	@Autowired
	private InvestorAccountService investorAccountService;

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
	
	@RequestMapping(value = { "holdList" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "持有人名录持有份额列表")
	public @ResponseBody ResponseEntity<InvestorHoldingDetListResp> holdList(HttpServletRequest request,
			@RequestParam String op,
			@And({ 
				@Spec(params = "accountOid", path = "baseAccount.investorAccount.oid", spec = Equal.class) 
			}) Specification<InvestorHolding> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "createTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		spec = Specifications.where(spec).and(new Specification<InvestorHolding>() {

			@Override
			public Predicate toPredicate(Root<InvestorHolding> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (op.equals("curr")) { // 已持有列表
					Expression<String> exp_state = root.get("state").as(String.class);
					predicate.add(exp_state.in(new Object[] { InvestorHolding.STATE_HOLDING, InvestorHolding.STATE_PARD_REDEEM }));

				} else if (op.equals("his")) { // 已平仓列表
					Expression<String> exp_state = root.get("state").as(String.class);
//					predicate.add(exp_state.in(new Object[] { InvestorHolding.STATE_CLOSE }));

					predicate.add(cb.equal(exp_state, InvestorHolding.STATE_CLOSE));
				} else {
					throw AMPException.getException("未知的操作类型[" + op + "]");
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		});
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		
		Page<InvestorHolding> pageData = investorHoldingService.list(spec, pageable);
		InvestorHoldingDetListResp resp = new InvestorHoldingDetListResp(pageData);
		return new ResponseEntity<InvestorHoldingDetListResp>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "orderList" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "持有人名录订单列表")
	public @ResponseBody ResponseEntity<InvestorOrderDetListResp> orderList(HttpServletRequest request,
			@And({ 
				@Spec(params = "accountOid", path = "account.oid", spec = Equal.class) 
			}) Specification<InvestorOrder> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "completeTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		spec = Specifications.where(spec).and(new Specification<InvestorOrder>() {

			@Override
			public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				Expression<String> exp_orderCate = root.get("orderCate").as(String.class);
				predicate.add(cb.equal(exp_orderCate, InvestorOrder.ORDER_CATE_Trade)); // 交易订单

				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		});
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		
		Page<InvestorOrder> pageData = investorOrderService.list(spec, pageable);
		InvestorOrderDetListResp resp = new InvestorOrderDetListResp(pageData);
		return new ResponseEntity<InvestorOrderDetListResp>(resp, HttpStatus.OK);
	}

}
