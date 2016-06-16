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

import com.guohuai.asset.manage.boot.product.ProductDetailResp;
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
	
	/**
	 * 作废
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<SpvOrderResp> delete(@RequestParam String oid) {
		String operator = super.getLoginAdmin();
		InvestorOrder investorOrder = this.spvOrderService.delete(oid, operator);
		return new ResponseEntity<SpvOrderResp>(new SpvOrderResp(investorOrder), HttpStatus.OK);
	}
	
	/**
	 * 审核确定
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/confirm", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<SpvOrderResp> confirm(@RequestParam String oid) {
		String operator = super.getLoginAdmin();
		InvestorOrder investorOrder = this.spvOrderService.confirm(oid, operator);
		return new ResponseEntity<SpvOrderResp>(new SpvOrderResp(investorOrder), HttpStatus.OK);
	}
	
	/**
	 * 根据资产池获取可以赎回金额
	 * @param assetPoolOid
	 * @return
	 */
	@RequestMapping(value = "/reemAmount", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public BigDecimal reemAmount(@RequestParam String assetPoolOid) {
		return this.spvOrderService.reemAmount(assetPoolOid);
	}
	
	/**
	 * 根据资产池获取关联的产品名称
	 * @param assetPoolOid
	 * @return
	 */
	@RequestMapping(value = "/product", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<ProductDetailResp> product(@RequestParam String assetPoolOid) {
		ProductDetailResp pr = this.spvOrderService.getProduct(assetPoolOid);
		return new ResponseEntity<ProductDetailResp>(pr, HttpStatus.OK);
	}
	
	/**
	 * spv订单列表
	 * @param request
	 * @param productName
	 * @param orderType 交易类型
	 * @param orderCate 订单类型
	 * @param orderStatus 订单状态
	 * @param entryStatus 订单入账状态
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
			@RequestParam String orderCate,
			@RequestParam String orderStatus,
			@RequestParam String entryStatus,
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
		Specification<InvestorOrder> spec = null;
		if(StringUtil.isEmpty(orderType)) {
			spec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_Invest), 
							cb.equal(root.get("orderType").as(String.class), InvestorOrder.ORDER_TYPE_Redeem));
				}
			};
		} else {
			spec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("orderType").as(String.class), orderType);
				}
			};
		}
		spec = Specifications.where(spec);
		
		Specification<InvestorOrder> orderCateSpec = null;
		if(StringUtil.isEmpty(orderCate)) {
			orderCateSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.equal(root.get("orderCate").as(String.class), InvestorOrder.ORDER_CATE_Trade), 
							cb.equal(root.get("orderCate").as(String.class), InvestorOrder.ORDER_CATE_Strike));
				}
			};
			spec = Specifications.where(spec).and(orderCateSpec);
		} else {
			orderCateSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("orderCate").as(String.class), orderCate);
				}
			};
			spec = Specifications.where(spec).and(orderCateSpec);
		}
		
		Specification<InvestorOrder> orderStatusSpec = null;
		if(!StringUtil.isEmpty(orderStatus)) {
			orderStatusSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("orderStatus").as(String.class), orderStatus);
				}
			};
			spec = Specifications.where(spec).and(orderStatusSpec);
		}
		
		Specification<InvestorOrder> entryStatusSpec = null;
		if(!StringUtil.isEmpty(entryStatus)) {
			entryStatusSpec = new Specification<InvestorOrder>() {
				@Override
				public Predicate toPredicate(Root<InvestorOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("entryStatus").as(String.class), entryStatus);
				}
			};
			spec = Specifications.where(spec).and(entryStatusSpec);
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
			spec = Specifications.where(spec).and(productNameSpec);
		}
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<SpvOrderResp> rep = this.spvOrderService.list(spec, pageable);
		return new ResponseEntity<PageResp<SpvOrderResp>>(rep, HttpStatus.OK);
	}
	

}
