package com.guohuai.asset.manage.boot.duration.target;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolForm;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.duration.order.FundForm;
import com.guohuai.asset.manage.boot.duration.order.OrderService;
import com.guohuai.asset.manage.boot.duration.order.TrustForm;
import com.guohuai.asset.manage.component.web.view.Response;

/**
 * 存续期--产品综合视图入口
 * @author star.zhu
 * 2016年5月17日
 */
@RestController
@RequestMapping(value = "/assetManagement/duration/product", produces = "application/json;charset=utf-8")
public class TargetController {
	
	@Autowired
	private TargetService productService;
	@Autowired
	private AssetPoolService assetPoolService;
	@Autowired
	private OrderService orderService;

	/**
	 * 资产池概要数据
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getAssetPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAssetPool(@RequestBody String pid) {
		AssetPoolForm form = assetPoolService.getById(pid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 预约中的 现金类管理工具 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getFundListForAppointment", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundListForAppointment(@RequestBody String pid) {
		List<FundForm> list = orderService.getFundListForAppointmentByPid(pid);
		Response r = new Response();
		r.with("result", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 成立中的 现金类管理工具 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getFundList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundList(@RequestBody String pid) {
		List<FundForm> list = orderService.getFundListByPid(pid);
		Response r = new Response();
		r.with("result", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 预约中的 信托（计划） 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getTrustListForAppointment", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustListForAppointment(@RequestBody String pid) {
		List<TrustForm> list = orderService.getTrustListForAppointmentByPid(pid);
		Response r = new Response();
		r.with("result", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 成立中的 信托（计划） 列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	@RequestMapping(value = "/getTrustList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustList(@RequestBody String pid) {
		List<TrustForm> list = orderService.getTrustListByPid(pid);
		Response r = new Response();
		r.with("result", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/getFundByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundByOid(@RequestBody String oid) {
		FundForm form = productService.getFundByOid(oid);
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
	public @ResponseBody ResponseEntity<Response> getTrustByOid(@RequestBody String oid) {
		TrustForm form = productService.getTrustByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
