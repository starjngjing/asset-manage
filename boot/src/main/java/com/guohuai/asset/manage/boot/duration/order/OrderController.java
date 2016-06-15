package com.guohuai.asset.manage.boot.duration.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.duration.capital.CapitalEntity;
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
	 * @param type
	 * 			申购方式：assetPool（资产池）；order（订单）
	 * @return
	 */
	@RequestMapping(value = "/purchaseForFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> purchaseForFund(FundForm form, String type) {
		orderService.purchaseForFund(form, "STAR", type);
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 */
	@RequestMapping(value = "/redeem", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> redeem(FundForm form) {
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
	public @ResponseBody ResponseEntity<Response> auditForFund(FundForm form) {
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
	public @ResponseBody ResponseEntity<Response> appointmentForFund(FundForm form) {
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
	public @ResponseBody ResponseEntity<Response> orderConfirmForFund(FundForm form) {
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
	public @ResponseBody ResponseEntity<Response> purchaseForTrust(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> auditForTrust(TrustForm form) {
		orderService.auditForTrust(form, "STAR");
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
	public @ResponseBody ResponseEntity<Response> appointmentForTrust(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> orderConfirmForTrust(TrustForm form) {
		orderService.orderConfirmForTrust(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 信托（计划）转入申购
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/purchaseForTrans", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> purchaseForTrans(TransForm form) {
		orderService.purchaseForTrans(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划）本息兑付
	 * @param from
	 */
	@RequestMapping(value = "/applyForIncome", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> applyForIncome(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> auditForIncome(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> orderConfirmForIncome(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> applyForTransfer(TrustForm form) {
		orderService.applyForTransfer(form, "STAR");
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
	public @ResponseBody ResponseEntity<Response> auditTrust(TrustForm form) {
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
	public @ResponseBody ResponseEntity<Response> orderConfirmForTransfer(TrustForm form) {
		orderService.orderConfirmForTransfer(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）的持仓信息
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/getFundByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundByOid(String oid) {
		FundForm form = orderService.getFundByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 货币基金（现金管理工具）的订单信息
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/getFundOrderByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getFundOrderByOid(String oid) {
		FundForm form = orderService.getFundOrderByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划） 的持仓信息
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/getTrustByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustByOid(String oid, String type) {
		TrustForm form = orderService.getTrustByOid(oid, type);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 信托（计划） 的订单信息
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/getTrustOrderByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTrustOrderByOid(String oid, String type) {
		TrustForm form = orderService.getTrustOrderByOid(oid, type);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 订单信息（资金明细）
	 * @param oid
	 * 			标的oid
	 */
	@RequestMapping(value = "/getTargetOrderByOidForCapital", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getTargetOrderByOidForCapital(String oid, String operation) {
		Response r = new Response();
		if (CapitalEntity.PURCHASE.equals(operation)
				|| CapitalEntity.REDEEM.equals(operation)) {
			FundForm form = orderService.getFundOrderByOid(oid);
			r.with("result", form);
		} else {
			TrustForm form = orderService.getTrustOrderByOid(oid, operation);
			r.with("result", form);
		}
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 逻辑删除订单
	 * @param oid
	 * 		订单oid
	 */
	@RequestMapping(value = "/updateOrder", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> updateOrder(String oid, String operation) {
		Response r = new Response();
		orderService.updateOrder(oid, operation);
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 纠偏现金管理工具的持有额度
	 * @param form
	 */
	@RequestMapping(value = "/updateFund", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> updateFund(FundForm form) {
		Response r = new Response();
		orderService.updateFund(form);
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 纠偏投资标的的持有额度
	 * @param form
	 */
	@RequestMapping(value = "/updateTrust", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> updateTrust(TrustForm form) {
		Response r = new Response();
		orderService.updateTrust(form);
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
