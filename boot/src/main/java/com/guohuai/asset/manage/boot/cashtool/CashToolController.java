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

import io.swagger.annotations.Api;


/**    
 * <p>Title: CashToolController.java</p>    
 * <p>现金管理工具操作相关接口 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 下午2:39:04   
 */   
@RestController
@RequestMapping(value = "/asset/boot/cashTool")
@Api("现金管理工具操作相关接口")
public class CashToolController {
	@Autowired
	CashToolDao cashToolDao;
	@Autowired
	CashToolService cashToolService;

}
