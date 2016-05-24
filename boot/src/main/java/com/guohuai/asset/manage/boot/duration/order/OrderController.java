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
	 * 货币基金（现金管理工具）申购审核
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/auditForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditForFund(@RequestBody FundForm form) {
		orderService.auditForFund(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）资金预约
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/appointmentForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> appointmentForFund(@RequestBody FundForm form) {
		orderService.appointmentForFund(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）订单确认
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/orderConfirmForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> orderConfirmForFund(@RequestBody FundForm form) {
		orderService.orderConfirmForFund(form, "STAR");
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
	 * 信托（计划）申购 审核
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/auditForTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditForTrust(@RequestBody TrustForm form) {
		orderService.purchaseForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 信托（计划）申购 预约
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/appointmentForTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> appointmentForTrust(@RequestBody TrustForm form) {
		orderService.appointmentForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 信托（计划）申购 确认
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/orderConfirmForTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> orderConfirmForTrust(@RequestBody TrustForm form) {
		orderService.orderConfirmForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）本息兑付
	 * @param from
	 */
	@RequestMapping(value = "/applyForIncome", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> applyForIncome(@RequestBody TrustForm form) {
		orderService.applyForIncome(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）本息兑付 审核
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/auditForIncome", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditForIncome(@RequestBody TrustForm form) {
		orderService.auditForIncome(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）本息兑付 确认
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/orderConfirmForIncome", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> orderConfirmForIncome(@RequestBody TrustForm form) {
		orderService.orderConfirmForIncome(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	@RequestMapping(value = "/applyForTransfer", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> applyForTransfer(@RequestBody TrustForm form) {
		orderService.transfer(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）转让 审核
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/auditForTransfer", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditTrust(@RequestBody TrustForm form) {
		orderService.auditForTransfer(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）转让 确认
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/orderConfirmForTransfer", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> orderConfirmForTransfer(@RequestBody TrustForm form) {
		orderService.orderConfirmForTransfer(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
