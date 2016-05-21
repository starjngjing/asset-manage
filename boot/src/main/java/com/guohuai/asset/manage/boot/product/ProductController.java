package com.guohuai.asset.manage.boot.product;

import java.text.ParseException;
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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;
import io.swagger.annotations.Api;

@Api("产品操作相关接口")
@RestController
@RequestMapping(value = "/ams/product", produces = "application/json")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;
	
	/**
	 * 新加定期产品
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/save/periodic", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> savePeriodic(@Valid SavePeriodicProductForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		if(Product.DATE_TYPE_ManualInput.equals(form.getRaiseStartDateType()) && StringUtil.isEmpty(form.getRaiseStatrtDate())) {
			throw AMPException.getException(90009);
		}
		BaseResp repponse = this.productService.savePeriodic(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 新加活期产品
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/save/current", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> saveCurrent(@Valid SaveCurrentProductForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		if(Product.DATE_TYPE_ManualInput.equals(form.getSetupDateType()) && StringUtil.isEmpty(form.getSetupDate())) {
			throw AMPException.getException(90010);
		}
		BaseResp repponse = this.productService.saveCurrent(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<ProductResp> delete(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		Product product = this.productService.delete(oid, operator);
		return new ResponseEntity<ProductResp>(new ProductResp(product), HttpStatus.OK);
	}
	
	/**
	 * 更新定期产品
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/update/periodic", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> updatePeriodic(@Valid SavePeriodicProductForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		if(Product.DATE_TYPE_ManualInput.equals(form.getRaiseStartDateType()) && StringUtil.isEmpty(form.getRaiseStatrtDate())) {
			throw AMPException.getException(90009);
		}
			
		BaseResp repponse = this.productService.updatePeriodic(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 更新活期产品
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/update/current", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> updateCurrent(@Valid SaveCurrentProductForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		if(Product.DATE_TYPE_ManualInput.equals(form.getSetupDateType()) && StringUtil.isEmpty(form.getSetupDate())) {
			throw AMPException.getException(90010);
		}
		BaseResp repponse = this.productService.updateCurrent(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品明细
	 * @param oid 产品类型的oid
	 * @return {@link ResponseEntity<ProductRep>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/detail", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<ProductDetailResp> detail(@RequestParam(required = true) String oid) {
		ProductDetailResp pr = this.productService.read(oid);
		return new ResponseEntity<ProductDetailResp>(pr, HttpStatus.OK);
	}
	
	/**
	 * 产品s申请列表查询
	 * @param request
	 * @param spec
	 * @param page 第几页
	 * @param rows 每页显示多少记录数
	 * @param sort 排序字段 update
	 * @param order 排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<ProductQueryRep>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/apply/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<ProductResp>> applyList(HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String type,
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
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Nocommit));
			}
		};
		spec = Specifications.where(spec);
				
		Specification<Product> nameSpec = null;
		if(!StringUtil.isEmpty(name)) {
			nameSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("name").as(String.class), "%" + name + "%"), cb.like(root.get("fullName").as(String.class), "%" + name + "%"));
				}
			};
			spec = Specifications.where(spec).and(nameSpec);
		}
		Specification<Product> typeSpec = null;
		if(!StringUtil.isEmpty(type)) {
			typeSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("type").get("oid").as(String.class), type);
				}
			};
			spec = Specifications.where(spec).and(typeSpec);
		}
		
		
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductResp> rep = this.productService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductResp>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 产品查询
	 * @param request
	 * @param spec
	 * @param page 第几页
	 * @param rows 每页显示多少记录数
	 * @param sort 排序字段 update
	 * @param order 排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<ProductQueryRep>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/audit/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<ProductResp>> auditList(HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String type,
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
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Auditing));
			}
		};
		spec = Specifications.where(spec);
				
		Specification<Product> nameSpec = null;
		if(!StringUtil.isEmpty(name)) {
			nameSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("name").as(String.class), "%" + name + "%"), cb.like(root.get("fullName").as(String.class), "%" + name + "%"));
				}
			};
			spec = Specifications.where(spec).and(nameSpec);
		}
		Specification<Product> typeSpec = null;
		if(!StringUtil.isEmpty(type)) {
			typeSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("type").get("oid").as(String.class), type);
				}
			};
			spec = Specifications.where(spec).and(typeSpec);
		}
		
		
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductResp> rep = this.productService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductResp>>(rep, HttpStatus.OK);
	}
	
	
	/**
	 * 产品查询
	 * @param request
	 * @param spec
	 * @param page 第几页
	 * @param rows 每页显示多少记录数
	 * @param sort 排序字段 update
	 * @param order 排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<ProductQueryRep>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/check/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<ProductResp>> checkList(HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String type,
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
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Reviewing));
			}
		};
		spec = Specifications.where(spec);
				
		Specification<Product> nameSpec = null;
		if(!StringUtil.isEmpty(name)) {
			nameSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("name").as(String.class), "%" + name + "%"), cb.like(root.get("fullName").as(String.class), "%" + name + "%"));
				}
			};
			spec = Specifications.where(spec).and(nameSpec);
		}
		Specification<Product> typeSpec = null;
		if(!StringUtil.isEmpty(type)) {
			typeSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("type").get("oid").as(String.class), type);
				}
			};
			spec = Specifications.where(spec).and(typeSpec);
		}
		
		
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductResp> rep = this.productService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductResp>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 产品查询
	 * @param request
	 * @param spec
	 * @param page 第几页
	 * @param rows 每页显示多少记录数
	 * @param sort 排序字段 update
	 * @param order 排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<ProductQueryRep>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/approve/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<ProductResp>> approveList(HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String type,
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
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Approvaling));
			}
		};
		spec = Specifications.where(spec);
				
		Specification<Product> nameSpec = null;
		if(!StringUtil.isEmpty(name)) {
			nameSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("name").as(String.class), "%" + name + "%"), cb.like(root.get("fullName").as(String.class), "%" + name + "%"));
				}
			};
			spec = Specifications.where(spec).and(nameSpec);
		}
		Specification<Product> typeSpec = null;
		if(!StringUtil.isEmpty(type)) {
			typeSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("type").get("oid").as(String.class), type);
				}
			};
			spec = Specifications.where(spec).and(typeSpec);
		}
		
		
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductResp> rep = this.productService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductResp>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 产品提交审核
	 * @param oids 产品oids
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/aduit/apply", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> aduitApply(@RequestParam String oids) throws ParseException {
		String operator = super.getLoginAdmin();
		List<String> oidlist = JSON.parseArray(oids, String.class);
		BaseResp repponse = this.productService.aduitApply(oidlist, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品审核通过
	 * @param oids 产品oids
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/aduit/approve", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> aduitApprove(@RequestParam(required = true) String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.aduitApprove(oid, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品审核不通过
	 * @param oid 产品oid
	 * @param auditComment 审核不通过备注
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 * @throws ParseException
	 */
	@RequestMapping(value = "/aduit/reject", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> aduitReject(@RequestParam(required = true) String oid,@RequestParam(required = true) String auditComment) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.aduitReject(oid, auditComment, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	
	/**
	 * 产品复核通过
	 * @param oids 产品oid
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/review/approve", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> reviewApprove(@RequestParam(required = true) String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.reviewApprove(oid, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品复核不通过
	 * @param oid 产品oid
	 * @param auditComment 审核不通过备注
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 * @throws ParseException
	 */
	@RequestMapping(value = "/review/reject", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> reviewReject(@RequestParam(required = true) String oid,@RequestParam(required = true) String auditComment) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.reviewReject(oid, auditComment, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品准入通过
	 * @param oids 产品oid
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/admit/approve", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> admitApprove(@RequestParam(required = true) String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.admitApprove(oid, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品准入不通过
	 * @param oid 产品oid
	 * @param auditComment 审核不通过备注
	 * @return {@link ResponseEntity<BaseResp>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 * @throws ParseException
	 */
	@RequestMapping(value = "/admit/reject", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> admitReject(@RequestParam(required = true) String oid,@RequestParam(required = true) String auditComment) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productService.admitReject(oid, auditComment, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
}
