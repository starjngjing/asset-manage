package com.guohuai.asset.manage.boot.investment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;



/**    
 * 投资标的库控制器
 * <p>Title: InvestmentPoolController.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 上午9:24:14   
 */
@RestController
@RequestMapping("/asset/hill/investmentPool")
@Api("投资标的库操作相关接口")
public class InvestmentPoolController extends BaseController {
	@ApiOperation(value = "登录", extensions = {
			@Extension(properties = { @ExtensionProperty(name = "x-code", value = "1001,1002"),
					@ExtensionProperty(name = "x-ref", value = "innerResp") }) })
	@RequestMapping(method = RequestMethod.GET)
	public void listinvestment() {

	}
	
}
