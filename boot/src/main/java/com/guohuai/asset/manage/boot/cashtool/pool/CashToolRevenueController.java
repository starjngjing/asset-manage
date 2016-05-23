/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.cashtool.pool;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolListResp;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.resp.CommonResp;
import com.guohuai.asset.manage.component.util.Section;
import com.guohuai.asset.manage.component.web.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;


/**    
 * <p>Title: CashToolRevenueController.java</p>    
 * <p>现金管理工具收益相关接口 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 下午2:39:04   
 */   
@RestController
@RequestMapping(value = "/ams/boot/cashToolRevenue")
@Api("现金管理工具收益相关接口")
@Slf4j
public class CashToolRevenueController extends BaseController {
	@Autowired
	CashtoolRevenueDao dashToolRevenueDao;
	@Autowired
	CashToolService cashToolService;
	@Autowired
	CashtoolRevenueService cashToolRevenueService;

	

	/**
	 * 现金管理工具收益列表
	 * 
	 * @Title: listCashTool
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param request
	 * @param spec
	 * @param page
	 * @param size
	 * @param sortDirection
	 * @param sortField
	 * @return ResponseEntity<CashToolListResp> 返回类型
	 */
	@RequestMapping(value = "listCashToolRevenue", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "现金管理工具收益列表")
	public @ResponseBody ResponseEntity<CashToolRevenueListResp> listCashToolRevenue(HttpServletRequest request,
			@And({	
				@Spec(params = "cashtoolOid", path = "cashTool.oid", spec = Equal.class) 
			}) Specification<CashToolRevenue> spec,
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

		Page<CashToolRevenue> pageData = cashToolRevenueService.getCashToolRevenueList(spec, pageable);

		CashToolRevenueListResp resp = new CashToolRevenueListResp(pageData);
		return new ResponseEntity<CashToolRevenueListResp>(resp, HttpStatus.OK);
	}

}
