/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.cashtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.resp.CommonResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**    
 * <p>Title: CashToolController.java</p>    
 * <p>现金管理工具操作相关接口 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 下午2:39:04   
 */   
@RestController
@RequestMapping(value = "/ams/boot/cashTool")
@Api("现金管理工具操作相关接口")
public class CashToolController {
	@Autowired
	CashToolDao cashToolDao;
	@Autowired
	CashToolService cashToolService;

	/**
	 * 现金管理工具保存
	 * @Title: save 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param cashTool
	 * @return CommonResp    返回类型
	 */
	@RequestMapping("save")
	@ApiOperation(value = "现金管理工具保存")
	public CommonResp save(CashTool cashTool){
		cashTool = cashToolService.save(cashTool);
		return CommonResp.builder().errorMessage("现金管理工具保存成功!").attached(cashTool.getOid()).build();
	}
}
