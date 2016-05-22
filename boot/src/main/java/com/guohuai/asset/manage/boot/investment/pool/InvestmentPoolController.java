package com.guohuai.asset.manage.boot.investment.pool;

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

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentListResp;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.boot.investment.TargetIncome;
import com.guohuai.asset.manage.boot.investment.TargetIncomeService;
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
 * 投资标的库控制器
 * <p>
 * Title: InvestmentPoolController.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author vania
 * @version 1.0
 * @created 2016年5月17日 上午9:24:14
 */
@RestController
@RequestMapping("/ams/boot/investmentPool")
@Api("投资标的库操作相关接口")
@Slf4j
public class InvestmentPoolController extends BaseController {
	@Autowired
	InvestmentPoolService investmentPoolService;
	@Autowired
	TargetIncomeService targetIncomeService;

	/**
	 * 投资标的库管理列表
	 * 
	 * @Title: investmentPoolList
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param request
	 * @param spec
	 * @param page
	 * @param size
	 * @param sortDirection
	 * @param sortField
	 * @return ResponseEntity<InvestmentListResp> 返回类型
	 */
	@RequestMapping(value = { "poolList"}, method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "标的库列表")
	public @ResponseBody ResponseEntity<InvestmentListResp> investmentPoolList(HttpServletRequest request,
			@RequestParam() String op,
			@And({	
				@Spec(params = "name", path = "name", spec = Like.class), 
				@Spec(params = "type", path = "type", spec = Equal.class) 
			}) Specification<Investment> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "updateTime") String sortField) {
		log.info("标的库查询op=" + op);

		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		
		// 根据不同的操作拼接条件
		spec = Specifications.where(spec).and(new Specification<Investment>() {
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				if (op.equals("storageList")) { // 标的库列表
					predicate.add(cb.equal(root.get("state").as(String.class), Investment.INVESTMENT_STATUS_collecting));
				} else if (op.equals("holdList")) { // 已持有列表
					predicate.add(cb.equal(root.get("state").as(String.class), Investment.INVESTMENT_STATUS_collecting));
				} else if (op.equals("noHoldList")) { // 未持有列表
					predicate.add(cb.equal(root.get("state").as(String.class), Investment.INVESTMENT_STATUS_collecting));
				} else if (op.equals("historyList")) { // 历史列表
					predicate.add(cb.equal(root.get("state").as(String.class), Investment.INVESTMENT_STATUS_invalid)); // 作废
				} else{
					throw AMPException.getException("未知的操作类型[" + op + "]"); 
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		});
		
		String raiseScope = request.getParameter("raiseScope");
		spec = this.buildSpec(spec, "raiseScope", raiseScope);		

		String lifed = request.getParameter("lifed");
		spec = this.buildSpec(spec, "lifed", lifed);		

		String expAror = request.getParameter("expAror");
		spec = this.buildSpec(spec, "expAror", expAror);
		
		Page<Investment> pageData = investmentPoolService.getInvestmentList(spec, pageable);

		InvestmentListResp resp = new InvestmentListResp(pageData);
		return new ResponseEntity<InvestmentListResp>(resp, HttpStatus.OK);
	}

	private Specification<Investment> buildSpec(Specification<Investment> spec, String attr, String value) {
		if (StringUtils.isNotBlank(value)) {
			spec = Specifications.where(spec).and(new Specification<Investment>() {
				@Override
				public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<Investment> section = new Section<Investment>(value);
					return section.build(root, cb, attr);
				}
			});
		}
		return spec;
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
		this.investmentPoolService.establish(form);
		return CommonResp.builder().errorMessage("标的成立成功！").attached("").build();
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
		this.investmentPoolService.unEstablish(form);
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
		TargetIncome interest = targetIncomeService.save(interestForm);
		return CommonResp.builder().errorMessage("投资标的本息兑付成功！").attached(interest.getOid()).build();
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
	public CommonResp overdue(@Valid OverdueForm form) {

		return CommonResp.builder().errorMessage("标的逾期登记成功！").attached("").build();
	}

}
