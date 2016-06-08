package com.guohuai.asset.manage.boot.duration.target;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolForm;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.duration.assetPool.scope.ScopeService;
import com.guohuai.asset.manage.boot.duration.order.FundForm;
import com.guohuai.asset.manage.boot.duration.order.OrderService;
import com.guohuai.asset.manage.boot.duration.order.TransForm;
import com.guohuai.asset.manage.boot.duration.order.TrustForm;
import com.guohuai.asset.manage.component.web.view.Response;

/**
 * 存续期--产品综合视图入口
 * @author star.zhu
 * 2016年5月17日
 */
@RestController
@RequestMapping(value = "/ams/duration/product", produces = "application/json;charset=utf-8")
public class TargetController {
	
	@Autowired
	private TargetService targetService;
	@Autowired
	private AssetPoolService assetPoolService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ScopeService scopeService;

	/**
	 * 资产池概要数据
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getAssetPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAssetPool(@RequestParam String pid) {
		AssetPoolForm form = assetPoolService.getPoolByOid(pid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 可申购的标的列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getTargetList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTargetList(@RequestParam String pid) {
		String[] scopes = scopeService.getScopes(pid);
		List<FundForm> fundList = targetService.getFundListByScopes(null);
		List<TrustForm> trustList = targetService.getTrustListByScopes(scopes);
		List<TransForm> transList = targetService.getTransListByScopes(scopes);
		Response r = new Response();
		r.with("fund", fundList);
		r.with("trust", trustList);
		r.with("trans", transList);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 预约中的 现金类管理工具 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getFundListForAppointment", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundListForAppointment(@RequestParam String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		
		List<FundForm> list = orderService.getFundListForAppointmentByPid(pid, pageable);
		Response r = new Response();
		r.with("rows", list);
		r.with("total", list.size());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 成立中的 现金类管理工具 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getFundList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundList(@RequestParam String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "oid") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		
		List<FundForm> list = orderService.getFundListByPid(pid, pageable);
		Response r = new Response();
		r.with("rows", list);
		r.with("total", list.size());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 预约中的 信托（计划） 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getTrustListForAppointment", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustListForAppointment(@RequestParam String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
//		Direction sortDirection = Direction.DESC;
//		if (!"desc".equals(sort)) {
//			sortDirection = Direction.ASC;
//		}
//		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		
		List<TrustForm> list = orderService.getTrustListForAppointmentByPid(pid);
		Response r = new Response();
		r.with("rows", list);
		r.with("total", list.size());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 成立中的 信托（计划） 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getTrustList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustList(@RequestParam String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "oid") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		
		List<TrustForm> list = orderService.getTrustListByPid(pid, pageable);
		Response r = new Response();
		r.with("rows", list);
		r.with("total", list.size());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/getFundByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundByOid(@RequestParam String oid) {
		FundForm form = targetService.getFundByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/getTrustByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustByOid(@RequestParam String oid) {
		TrustForm form = targetService.getTrustByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
