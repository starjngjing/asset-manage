package com.guohuai.asset.manage.boot.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.guohuai.asset.manage.component.web.BaseController;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import java.math.BigDecimal;
import java.text.ParseException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.RequestParam;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;


@Api("SPV订单操作相关接口")
@RestController
@RequestMapping(value = "/ams/spv/order", produces = "application/json")
public class SpvOrderController extends BaseController {
	
	@Autowired
	private SpvOrderService spvOrderService;
	
	/**
	 * 新加订单
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> saveInvestorOrder(@Valid SaveSpvOrderForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.spvOrderService.saveSpvOrder(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<SpvOrderResp> delete(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		InvestorOrder investorOrder = this.spvOrderService.delete(oid, operator);
		return new ResponseEntity<SpvOrderResp>(new SpvOrderResp(investorOrder), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/confirm", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<SpvOrderResp> confirm(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		InvestorOrder investorOrder = this.spvOrderService.confirm(oid, operator);
		return new ResponseEntity<SpvOrderResp>(new SpvOrderResp(investorOrder), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/reemAmount", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public BigDecimal reemAmount(@RequestParam(required = true) String assetPoolOid) {
		return this.spvOrderService.reemAmount(assetPoolOid);
	}
	
	
	/**
	 * spv订单列表
	 * @param request
	 * @param productName
	 * @param orderType
	 * * @param orderStatus
	 * @param page 第几页
	 * @param rows  每页显示多少记录数
	 * @param sort 排序字段 update
	 * @param order 排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<InvestorOrderResp>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<SpvOrderResp>> indetList(HttpServletRequest request,
			@RequestParam String productName,
			@RequestParam String orderType,
			@RequestParam String orderStatus,
			@RequestParam int page, 
			@RequestParam int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sort,
			@RequestParam(required = false, defaultValue = "desc") String order) {
		
		if (page < 1) {
			page = 1;
		}
		if (rows < 1) {
			rows = 1;
		}
		
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(order)) {
			sortDirection = Direction.ASC;
		}
		Specification<InvestorOrder> orderTypeSpec = null;
		if(StringUtil.isEmpty(orderType)) {
			orderTypeSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_Invest), 
							cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_PartRedeem),
							cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_FullRedeem));
				}
			};
		} else {
			orderTypeSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					if("REDEEM".equals(orderType)) {
						return cb.or(cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_PartRedeem),
								cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_FullRedeem));
					} else {
						return cb.equal(root.get("orderType").as(String.class), orderType);
					}
					
				}
			};
		}
		orderTypeSpec = Specifications.where(orderTypeSpec);
		
		Specification<InvestorOrder> orderStatusSpec = null;
		if(!StringUtil.isEmpty(orderStatus)) {
			orderStatusSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("orderStatus").as(String.class), orderStatus);
				}
			};
			orderTypeSpec = Specifications.where(orderTypeSpec).and(orderStatusSpec);
		}
				
		Specification<InvestorOrder> productNameSpec = null;
		if(!StringUtil.isEmpty(productName)) {
			productNameSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("account").get("product").get("name").as(String.class), "%" + productName + "%"), 
							cb.like(root.get("account").get("product").get("fullName").as(String.class), "%" + productName + "%"));
				}
			};
			orderTypeSpec = Specifications.where(orderTypeSpec).and(productNameSpec);
		}
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<SpvOrderResp> rep = this.spvOrderService.list(orderTypeSpec, pageable);
		return new ResponseEntity<PageResp<SpvOrderResp>>(rep, HttpStatus.OK);
	}
	

}
