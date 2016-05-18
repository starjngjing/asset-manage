package com.guohuai.asset.manage.boot.investment.pool;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

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

import com.guohuai.asset.manage.boot.investment.Interest;
import com.guohuai.asset.manage.boot.investment.InterestService;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentListResp;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
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
	InvestmentService investmentService;
	@Autowired
	InterestService interestService;

	/**
	 * 标的成立管理列表
	 * 
	 * @Title: listinvestment
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
	@RequestMapping(value = "listinvestment", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "标的成立管理列表")
	public @ResponseBody ResponseEntity<InvestmentListResp> listinvestment(HttpServletRequest request,
			@And({ @Spec(params = "name", path = "name", spec = Like.class), @Spec(params = "type", path = "type", spec = Equal.class) }) Specification<Investment> spec,
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
		
		String raiseScope = request.getParameter("raiseScope");
		if (StringUtils.isNotBlank(raiseScope)) {
			spec = Specifications.where(spec).and(new Specification<Investment>() {
				@Override
				public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<Investment> section = new Section<Investment>(raiseScope);
					return section.build(root, cb, "raiseScope");
				}
			});
		}

		String lifed = request.getParameter("lifed");
		if (StringUtils.isNotBlank(lifed)) {
			spec = Specifications.where(spec).and(new Specification<Investment>() {
				@Override
				public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<Investment> section = new Section<Investment>(lifed);
					return section.build(root, cb, "lifed");
				}
			});
		}

		String expAror = request.getParameter("expAror");
		if (StringUtils.isNotBlank(expAror)) {
			spec = Specifications.where(spec).and(new Specification<Investment>() {
				@Override
				public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Section<Investment> section = new Section<Investment>(expAror);
					return section.build(root, cb, "expAror");
				}
			});
		}

		Page<Investment> pageData = investmentService.getInvestmentList(spec, pageable);

		InvestmentListResp resp = new InvestmentListResp(pageData);
		return new ResponseEntity<InvestmentListResp>(resp, HttpStatus.OK);
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
	public CommonResp establish() {
		return CommonResp.builder().errorCode(1).errorMessage("标的成立成功！").attached("").build();
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
	public CommonResp unEstablish() {
		return CommonResp.builder().errorCode(1).errorMessage("标的不成立成功！").attached("").build();
	}

	/**
	 * 投资标的本息兑付
	 * 
	 * @Title: interestSave
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param interest
	 * @return CommonResp 返回类型
	 */
	@RequestMapping("interestSave")
	@ApiOperation(value = "投资标的本息兑付")
	public CommonResp interestSave(Interest interest) {
		interest = interestService.save(interest);
		return CommonResp.builder().errorCode(1).errorMessage("投资标的本息兑付成功！").attached(interest.getOid()).build();
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

		return CommonResp.builder().errorCode(1).errorMessage("标的逾期登记成功！").attached("").build();
	}

}
