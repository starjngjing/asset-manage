package com.guohuai.asset.manage.boot.product;

import java.text.ParseException;

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
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;

import io.swagger.annotations.Api;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

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
		BaseResp repponse = this.productService.saveCurrent(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
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
		BaseResp repponse = this.productService.updateCurrent(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	/**
	 * 产品明细
	 * @param oid 产品类型的oid
	 * @return {@link ResponseEntity<ProductRep>} ,如果返回的errCode属性等于0表示成功，否则表示失败，失败原因在errMessage里面体现 
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ProductResp> detail(@RequestParam(required = true) String oid) {
		ProductResp pr = this.productService.read(oid);
		return new ResponseEntity<ProductResp>(pr, HttpStatus.OK);
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
	@RequestMapping(value = "/search/list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<PageResp<ProductResp>> searchList(HttpServletRequest request,
			@Conjunction(and = { @Spec(path = "name", params = "name", spec = Like.class),
					@Spec(path = "status", params = "status", spec = In.class), 
					@Spec(path = "type.oid", params = "type", spec = In.class) }, value = { }) Specification<Product> spec,
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
		
		spec = Specifications.where(spec);
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sort)));
		PageResp<ProductResp> rep = this.productService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductResp>>(rep, HttpStatus.OK);
	}
	
}
