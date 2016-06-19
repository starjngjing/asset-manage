package com.guohuai.asset.manage.boot.duration.fact.calibration;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.Response;

@RestController
@RequestMapping(value = "/ams/duration/market", produces = "application/json;charset=utf-8")
public class MarketAdjustController extends BaseController {

	@Autowired
	private MarketAdjustService adjustService;
	
	/**
	 * 市值校准录入 详情表单
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/getMarketAdjustData", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getMarketAdjustData(@RequestParam String pid) {
		MarketAdjustForm form = adjustService.getMarketAdjust(pid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 市值校准录入
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/saveMarketAdjust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> purchaseForFund(MarketAdjustForm form) {
		form.setStatus(MarketAdjustEntity.CREATE);
		form.setCreateTime(new Timestamp(System.currentTimeMillis()));
		form.setCreator(super.getLoginAdmin());
		adjustService.saveMarketAdjust(form);
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 市值校准记录详情
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/getMarketAdjust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getMarketAdjust(@RequestParam String oid) {
		MarketAdjustEntity market = adjustService.findOne(oid);
		Response r = new Response();
		r.with("result", market);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 市值校准录入审核
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/auditMarketAdjust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditMarketAdjust(@RequestParam String oid, @RequestParam String type) {
		adjustService.auditMarketAdjust(oid, type, super.getLoginAdmin());
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 查询当天的订单状态
	 * -1：未审核；0：未录入；1：已通过
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/getMarketAdjustStuts", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getMarketAdjustStatus(@RequestParam String pid) {
		int stutas = adjustService.getMarketAdjustStatus(pid);
		Response r = new Response();
		r.with("result", stutas);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 市值校准录入删除
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/deleteMarketAdjust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> deleteMarketAdjust(@RequestParam String oid) {
		MarketAdjustEntity market = adjustService.findOne(oid);
		market.setStatus(MarketAdjustEntity.DELETE);
		market.setAuditor(super.getLoginAdmin());
		market.setAuditTime(new Timestamp(System.currentTimeMillis()));
		adjustService.save(market);
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 市值校准记录 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getMarketAdjustList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getMarketAdjustList(@RequestParam String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		
		Page<MarketAdjustEntity> list = adjustService.getMarketAdjustList(pid, pageable);
		Response r = new Response();
		r.with("rows", list.getContent());
		r.with("total", list.getTotalElements());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 市值校准 - 收益率
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/getYield", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getYield(@RequestParam String pid) {
		List<JSONObject> obj = adjustService.getListForYield(pid);
		Response r = new Response();
		r.with("result", obj);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
