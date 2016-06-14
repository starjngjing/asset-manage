package com.guohuai.asset.manage.boot.system.config.risk.warning.collect.handle;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.guohuai.asset.manage.boot.system.config.risk.warning.collect.RiskWarningCollect;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

/**
 * 风控处置
 * 
 * @author lirong
 *
 */
@RestController
@RequestMapping(value = "/ams/system/ccr/warning/collect/handle", produces = "application/json;charset=utf-8")
public class RiskWarningHandleController extends BaseController {

	@Autowired
	RiskWarningHandleService riskWarningHandleService;

	/**
	 * 列表
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param sortDirection
	 * @param sortField
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<RiskWarningHandleListResp> list(HttpServletRequest request,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows,
			@RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "createTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		Specification<RiskWarningCollect> spec = new Specification<RiskWarningCollect>() {
			@Override
			public Predicate toPredicate(Root<RiskWarningCollect> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.notEqual(root.get("wlevel").as(String.class), RiskWarningCollect.COLLECT_LEVEL_NONE);
			}
		};
		List<RiskWarningHandleDetResp> res = riskWarningHandleService.list(spec, pageable);
		return new ResponseEntity<RiskWarningHandleListResp>(new RiskWarningHandleListResp(res), HttpStatus.OK);
	}

	/**
	 * 风控处置
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "handle", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> handle(@Valid RiskWarningHandleForm form) {
		String operator = super.getLoginAdmin();
		riskWarningHandleService.handle(form, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 历史处置全列表
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param sortDirection
	 * @param sortField
	 * @return
	 */
	@RequestMapping(value = "hisListAll", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<RiskWarningHandleHisListResp> hisListAll(HttpServletRequest request,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows,
			@RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "createTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		List<RiskWarningHandleHisDetResp> res = riskWarningHandleService.hisList(null, pageable);
		return new ResponseEntity<RiskWarningHandleHisListResp>(new RiskWarningHandleHisListResp(res), HttpStatus.OK);
	}

	/**
	 * 风控处置列表
	 * 
	 * @param request
	 * @param oid
	 * @param page
	 * @param rows
	 * @param sortDirection
	 * @param sortField
	 * @return
	 */
	@RequestMapping(value = "hisList", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<RiskWarningHandleHisListResp> hisList(HttpServletRequest request,
			@RequestParam(required = true) String oid, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "50") int rows, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "createTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 50;
		}
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(order));
		Specification<RiskWarningHandle> spec = new Specification<RiskWarningHandle>() {
			@Override
			public Predicate toPredicate(Root<RiskWarningHandle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("riskWarningCollect").get("relative").as(String.class), oid);
			}
		};
		List<RiskWarningHandleHisDetResp> res = riskWarningHandleService.hisList(spec, pageable);
		return new ResponseEntity<RiskWarningHandleHisListResp>(new RiskWarningHandleHisListResp(res), HttpStatus.OK);
	}

}
