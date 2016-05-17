package com.guohuai.asset.manage.boot.duration.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.view.Response;

/**
 * 存续期--订单视图入口
 * @author star.zhu
 * 2016年5月17日
 */
@RestController
@RequestMapping(value = "/ams/duration/order", produces = "application/json;charset=utf-8")
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	/**
	 * 货币基金（现金管理工具）申购
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/purchaseForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> purchaseForFund(@RequestBody FundForm form) {
		orderService.purchaseForFund(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 */
	@RequestMapping(value = "/redeem", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> redeem(@RequestBody FundForm form) {
		orderService.redeem(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 信托（计划）申购
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/purchaseForTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> purchaseForTrust(@RequestBody TrustForm form) {
		orderService.purchaseForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	@RequestMapping(value = "/transfer", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> transfer(@RequestBody TrustForm form) {
		orderService.transfer(form);
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）申购审核
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/auditFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditFund(@RequestBody FundForm form) {
		orderService.auditFund(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）申购审核
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/auditTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditTrust(@RequestBody TrustForm form) {
		orderService.auditTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）资金预约
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/cashAppointMentForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> cashAppointMentForFund(@RequestBody FundForm form) {
		orderService.cashAppointMentForFund(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/cashAppointMentForTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> cashAppointMentForTrust(@RequestBody TrustForm form) {
		orderService.cashAppointMentForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
