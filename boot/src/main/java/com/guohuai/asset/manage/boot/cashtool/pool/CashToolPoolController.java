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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import com.guohuai.asset.manage.boot.cashtool.CashToolDao;
import com.guohuai.asset.manage.boot.cashtool.CashToolListResp;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.boot.investment.pool.EstablishForm;
import com.guohuai.asset.manage.boot.investment.pool.TargetIncomeForm;
import com.guohuai.asset.manage.boot.investment.pool.UnEstablishForm;
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
 * <p>Title: CashToolController.java</p>    
 * <p>现金管理工具操作相关接口 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 下午2:39:04   
 */   
@RestController
@RequestMapping(value = "/ams/boot/cashToolPool")
@Api("现金管理工具操作相关接口")
@Slf4j
public class CashToolPoolController extends BaseController {
	@Autowired
	CashToolDao cashToolDao;
	@Autowired
	CashToolService cashToolService;

	

	/**
	 * 现金工具库管理列表
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
	@RequestMapping(value = "listCashTool", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "现金工具成立管理列表")
	public @ResponseBody ResponseEntity<CashToolListResp> listCashTool(HttpServletRequest request,
			@And({	@Spec(params = "secShortName", path = "secShortName", spec = Like.class),
					@Spec(params = "ticker", path = "ticker", spec = Like.class), 
					@Spec(params = "etfLof", path = "etfLof", spec = Equal.class) }) 
					Specification<CashTool> spec,
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

		// 拼接条件
		spec = Specifications.where(spec).and(new Specification<CashTool>() {
			@Override
			public Predicate toPredicate(Root<CashTool> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();

				predicate.add(cb.notEqual(root.get("state").as(String.class), CashTool.CASHTOOL_STATE_delete));
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		});

		// 最新流通份额
		String circulationShares = request.getParameter("circulationShares");
		if (StringUtils.isNotBlank(circulationShares)) {
			spec = Specifications.where(spec).and(new Specification<CashTool>() {
				@Override
				public Predicate toPredicate(Root<CashTool> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<CashTool> section = new Section<CashTool>(circulationShares);
					return section.build(root, cb, "circulationShares");
				}
			});
		}
		
		// 7日年化收益率
		String weeklyYield = request.getParameter("weeklyYield");
		if (StringUtils.isNotBlank(weeklyYield)) {
			spec = Specifications.where(spec).and(new Specification<CashTool>() {
				@Override
				public Predicate toPredicate(Root<CashTool> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<CashTool> section = new Section<CashTool>(weeklyYield);
					return section.build(root, cb, "weeklyYield");
				}
			});
		}

		Page<CashTool> pageData = cashToolService.getCashToolList(spec, pageable);

		CashToolListResp resp = new CashToolListResp(pageData);
		return new ResponseEntity<CashToolListResp>(resp, HttpStatus.OK);
	}

	
	
	/**
	 * 标的成立
	 * 
	 * @Title: establish
	 * @author vania
	 * @version 1.0 @see:
	 * @return CommonResp 返回类型
	 */
	@RequestMapping("establish")
	@ApiOperation(value = "标的成立")
	public CommonResp establish(@Valid EstablishForm form) {
		log.debug("投资标的成立接口!!!");
		String loginId = null; 
		try {
			loginId = super.getLoginAdmin();
		} catch (Exception e) {
			
		}
		form.setOperator(loginId);
//		this.cashToolService.establish(form);
		return CommonResp.builder().errorMessage("标的成立成功！").attached("").build();
	}
	
	
	/**
	 * 现金管理工具移除出库
	 * 
	 * @Title: removeCashTool
	 * @author vania
	 * @version 1.0 @see:
	 * @return CommonResp 返回类型
	 */
	@RequestMapping("removeCashTool")
	@ApiOperation(value = "现金管理工具移除出库")
	public CommonResp removeCashTool(String oid) {
		log.debug("现金管理工具移除出库接口!!!");
		String loginId = super.getLoginAdmin();
		log.debug("获取操作员id:" + loginId);
		this.cashToolService.remove(oid, loginId);
		return CommonResp.builder().errorMessage("移除出库成功！").attached("").build();
	}

	/**
	 * 标的不成立
	 * 
	 * @Title: unEstablish
	 * @author vania
	 * @version 1.0
	 * @see: @return CommonResp 返回类型 @throws
	 */
	@RequestMapping("unEstablish")
	@ApiOperation(value = "标的不成立")
	public CommonResp unEstablish(@Valid UnEstablishForm form) {
		log.debug("投资标的成立接口!!!");
		String loginId = null; 
		try {
			loginId = super.getLoginAdmin();
		} catch (Exception e) {
			
		}
		form.setOperator(loginId);
//		this.cashToolService.unEstablish(form);
		return CommonResp.builder().errorMessage("标的不成立成功！").attached("").build();
	}

	/**
	 * 投资标的本息兑付
	 * 
	 * @Title: interestSave
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param interest
	 * @return CommonResp 返回类型
	 */
	@RequestMapping("targetIncomeSave")
	@ApiOperation(value = "投资标的本息兑付")
	public CommonResp interestSave(@Valid TargetIncomeForm interestForm) {
		String loginId = null;
		try {
			loginId = super.getLoginAdmin();
		} catch (Exception e) {
			log.error("获取操作员失败, 原因: " + e.getMessage());
		}
		interestForm.setOperator(loginId);
//		TargetIncome interest = targetIncomeService.save(interestForm);
		return CommonResp.builder().errorMessage("投资标的本息兑付成功！").attached("").build();
	}

	/**
	 * 标的逾期
	 * 
	 * @Title: overdue
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param days
	 * @param rate
	 * @param overdueFine
	 * @return CommonResp 返回类型
	 */
	@RequestMapping("overdue")
	@ApiOperation(value = "标的逾期")
	public CommonResp overdue(Integer days, Double rate, BigDecimal overdueFine) {

		return CommonResp.builder().errorMessage("标的逾期登记成功！").attached("").build();
	}
}
