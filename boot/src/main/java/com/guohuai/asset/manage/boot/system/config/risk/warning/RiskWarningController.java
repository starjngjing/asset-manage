/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.system.config.risk.warning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.system.config.risk.cate.RiskCateService;
import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/ams/system/ccr/warning", produces = "application/json;charset=utf-8")
public class RiskWarningController extends BaseController {
	@Autowired
	RiskWarningService riskWarningService;
	@Autowired
	RiskCateService riskCateService;
}
