package com.guohuai.asset.manage.boot.investment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.resp.CommonResp;
import com.guohuai.asset.manage.component.web.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;



/**    
 * 投资标的库控制器
 * <p>Title: InvestmentPoolController.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 上午9:24:14   
 */
@RestController
@RequestMapping("/asset/boot/investmentPool")
@Api("投资标的库操作相关接口")
public class InvestmentPoolController extends BaseController {
	@Autowired
	InvestmentService investmentService;
		
	/**
	 * 标的成立管理列表
	 * @Title: listinvestment 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param request
	 * @param spec
	 * @param page
	 * @param size
	 * @param sortDirection
	 * @param sortField
	 * @return
	 * @return ResponseEntity<InvestmentListResp>    返回类型 
	 * @throws
	 */
	@RequestMapping(value = "listinvestment", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiOperation(value = "标的成立管理列表", extensions = {
			@Extension(properties = { @ExtensionProperty(name = "x-code", value = "1001,1002"),
					@ExtensionProperty(name = "x-ref", value = "innerResp") }) })
	public @ResponseBody ResponseEntity<InvestmentListResp> listinvestment(HttpServletRequest request,
			@And({ @Spec(path = "projectName", spec = Like.class), @Spec(path = "projectManager", spec = Equal.class),
					@Spec(path = "pjType", params = "pjType", spec = Equal.class) }) Specification<Investment> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int size, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "updateTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (size <= 0) {
			size = 50;
		}
		Pageable pageable = new PageRequest(page, size);
		
		Page<Investment> pageData = investmentService.getInvestmentList(spec, pageable);		
		
		InvestmentListResp resp = new InvestmentListResp(pageData);
		return new ResponseEntity<InvestmentListResp>(resp, HttpStatus.OK);
	}
	
	/**
	 * 标的成立
	 * @Title: establish 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @return
	 * @return CommonResp    返回类型 
	 * @throws
	 */
	@RequestMapping("establish")
	@ApiOperation(value = "标的成立", extensions = {
			@Extension(properties = { @ExtensionProperty(name = "x-code", value = "1001,1002"),
					@ExtensionProperty(name = "x-ref", value = "innerResp") }) })
	public CommonResp establish() {
		return CommonResp.builder().errorCode(1).errorMessage("标的成立成功！").attached("").build();
	}
	
	/**
	 * 标的不成立
	 * @Title: unEstablish 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @return
	 * @return CommonResp    返回类型 
	 * @throws
	 */
	@RequestMapping("unEstablish")
	@ApiOperation(value = "标的成立", extensions = {
			@Extension(properties = { @ExtensionProperty(name = "x-code", value = "1001,1002"),
					@ExtensionProperty(name = "x-ref", value = "innerResp") }) })
	public CommonResp unEstablish() {
		return CommonResp.builder().errorCode(1).errorMessage("标的不成立成功！").attached("").build();
	}
}
