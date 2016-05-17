package com.guohuai.asset.manage.boot.investment;

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

import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

/**
 * 投资标的管理
 * 
 * @author lirong
 *
 */
@RestController
@RequestMapping(value = "/asset/boot/investmentManage", produces = "application/json;charset=UTF-8")
public class InvestmentManageBootController extends BaseController {

	@Autowired
	private InvestmentService investmentService;

	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentListResp> list(HttpServletRequest request,
			@And({ @Spec(path = "investmentName", spec = Like.class),
					@Spec(path = "investmentType", spec = Equal.class),
					@Spec(path = "status", spec = Equal.class) }) Specification<Investment> spec,
			@RequestParam int page, @RequestParam int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		Page<Investment> entitys = investmentService.getInvestmentList(spec, pageable);
		InvestmentListResp resps = new InvestmentListResp(entitys);
		return new ResponseEntity<InvestmentListResp>(resps, HttpStatus.OK);
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<BaseResp> add(Investment investment) {
		String operator = "test";
		investmentService.saveInvestment(investment, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}
}
