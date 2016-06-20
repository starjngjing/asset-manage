package com.guohuai.asset.manage.boot.product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

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

import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;

import io.swagger.annotations.Api;

@Api("产品操作相关接口")
@RestController
@RequestMapping(value = "/ams/product/duration", produces = "application/json")
public class ProductDurationController {

	@Autowired
	private ProductDurationService productDurationService;

	/**
	 * 产品查询
	 * 
	 * @param request
	 * @param spec
	 * @param page
	 *            第几页
	 * @param rows
	 *            每页显示多少记录数
	 * @param sort
	 *            排序字段 update
	 * @param order
	 *            排序规则：升序还是降序 desc
	 * @return {@link ResponseEntity<PagesRep<ProductQueryRep>>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<PageResp<ProductLogListResp>> list(HttpServletRequest request, @RequestParam String name, @RequestParam String type, @RequestParam int page, @RequestParam int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sort, @RequestParam(required = false, defaultValue = "desc") String order) {

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
				return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Approval));
			}
		};
		spec = Specifications.where(spec);

		Specification<Product> nameSpec = null;
		if (!StringUtil.isEmpty(name)) {
			nameSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.or(cb.like(root.get("name").as(String.class), "%" + name + "%"), cb.like(root.get("fullName").as(String.class), "%" + name + "%"));
				}
			};
			spec = Specifications.where(spec).and(nameSpec);
		}
		Specification<Product> typeSpec = null;
		if (!StringUtil.isEmpty(type)) {
			typeSpec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("type").get("oid").as(String.class), type);
				}
			};
			spec = Specifications.where(spec).and(typeSpec);
		}

		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductLogListResp> rep = this.productDurationService.durationList(spec, pageable);
		return new ResponseEntity<PageResp<ProductLogListResp>>(rep, HttpStatus.OK);
	}
}
