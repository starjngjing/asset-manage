package com.guohuai.asset.manage.boot.duration.fact.income;

import java.text.ParseException;
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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;

@RestController
@RequestMapping(value = "/ams/duration/income", produces = "application/json;charset=utf-8")
public class IncomeDistributionController extends BaseController {
	
	@Autowired
	private IncomeDistributionService incomeService;
	@Autowired
	private AssetPoolService poolService;
	
	/**
	 * 收益分配弹出框详情查询
	 * @param assetPoolOid 资产池oid
	 * @return
	 */
	@RequestMapping(value = "/getIncomeAdjustData", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<IncomeAllocateCalcResp> getIncomeAdjustData(@RequestParam String assetPoolOid) {
		IncomeAllocateCalcResp resp = this.incomeService.getIncomeAdjustData(assetPoolOid);
		return new ResponseEntity<IncomeAllocateCalcResp>(resp, HttpStatus.OK);
	}
	
	/**
	 * 保存收益分配
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/saveIncomeAdjust", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> saveIncomeAdjust(@Valid IncomeAllocateForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.incomeService.saveIncomeAdjust(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	
	
	/**
	 * 收益分配详情
	 * @param oid  收益分配表oid
	 * @return
	 */
	@RequestMapping(value = "/getIncomeAdjust", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<IncomeDistributionResp> detail(@RequestParam String oid) {
		IncomeDistributionResp pr = this.incomeService.getIncomeAdjust(oid);
		return new ResponseEntity<IncomeDistributionResp>(pr, HttpStatus.OK);
	}
	
	/**
	 * 收益分配 列表
	 * @param request
	 * @param assetPoolOid 资产池id
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/getIncomeAdjustList", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<IncomeDistributionResp>> getIncomeAdjustList(HttpServletRequest request,
			@RequestParam String assetPoolOid,
			@RequestParam int page, 
			@RequestParam int rows,
			@RequestParam(required = false, defaultValue = "baseDate") String sort,
			@RequestParam(required = false, defaultValue = "desc") String order) {
		
		if (page < 1) {
			page = 1;
		}
		if (rows < 1) {
			rows = 1;
		}
		
		String pid = null;
		if(StringUtil.isEmpty(assetPoolOid)) {
			pid = poolService.getPid("");
		}
		final String apOid = StringUtil.isEmpty(assetPoolOid)==false?assetPoolOid:pid;
		
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(order)) {
			sortDirection = Direction.ASC;
		}
		Specification<IncomeAllocate> spec = new Specification<IncomeAllocate>() {
			@Override
			public Predicate toPredicate(Root<IncomeAllocate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.notEqual(root.get("incomeEvent").get("status").as(String.class), IncomeEvent.STATUS_Delete),
						cb.equal(root.get("incomeEvent").get("assetPool").get("oid").as(String.class), apOid));
			}
		};
		spec = Specifications.where(spec);
				
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<IncomeDistributionResp> rep = this.incomeService.getIncomeAdjustList(spec, pageable);
		return new ResponseEntity<PageResp<IncomeDistributionResp>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 收益分配审核通过
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/auditPassIncomeAdjust", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> auditPassIncomeAdjust(@RequestParam String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.incomeService.auditPassIncomeAdjust(oid, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 收益分配审核不通过
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/auditFailIncomeAdjust", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> auditFailIncomeAdjust(@RequestParam String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.incomeService.auditFailIncomeAdjust(oid, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 收益分配删除
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/deleteIncomeAdjust", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<IncomeDistributionResp> deleteIncomeAdjust(@RequestParam String oid) {
		String operator = super.getLoginAdmin();
		IncomeAllocate incomeAllocate = this.incomeService.deleteIncomeAdjust(oid, operator);
		return new ResponseEntity<IncomeDistributionResp>(new IncomeDistributionResp(incomeAllocate), HttpStatus.OK);
	}

}
