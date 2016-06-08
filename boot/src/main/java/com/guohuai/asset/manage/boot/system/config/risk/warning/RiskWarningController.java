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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.system.config.risk.cate.RiskCateService;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateForm;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateOption;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateResp;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateService;
import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/ams/system/ccr/warning", produces = "application/json;charset=utf-8")
public class RiskWarningController extends BaseController {
	@Autowired
	RiskWarningService riskWarningService;
	@Autowired
	RiskCateService riskCateService;

	@Autowired
	private RiskIndicateService riskIndicateService;

	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskIndicateResp>> search(@RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String keyword) {
		super.checkLogin();
		List<RiskIndicateResp> list = this.riskIndicateService.search(type, keyword);
		return new ResponseEntity<List<RiskIndicateResp>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskIndicateResp> save(RiskIndicateForm form) {
		super.checkLogin();
		RiskIndicate i = this.riskIndicateService.save(form);
		return new ResponseEntity<RiskIndicateResp>(new RiskIndicateResp(i), HttpStatus.OK);
	}

	@RequestMapping(value = "/enable", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskIndicateResp> enable(@RequestParam String oid) {
		super.checkLogin();
		RiskIndicate i = this.riskIndicateService.enable(oid);
		return new ResponseEntity<RiskIndicateResp>(new RiskIndicateResp(i), HttpStatus.OK);
	}

	@RequestMapping(value = "/disable", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskIndicateResp> disable(@RequestParam String oid) {
		super.checkLogin();
		RiskIndicate i = this.riskIndicateService.disable(oid);
		return new ResponseEntity<RiskIndicateResp>(new RiskIndicateResp(i), HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskIndicateResp> delete(@RequestParam String oid) {
		super.checkLogin();
		RiskIndicate i = this.riskIndicateService.delete(oid);
		return new ResponseEntity<RiskIndicateResp>(new RiskIndicateResp(i), HttpStatus.OK);
	}

	@RequestMapping(value = "/options", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskIndicateOption>> options(@RequestParam String type) {
		super.checkLogin();
		List<RiskIndicateOption> list = this.riskIndicateService.options(type);
		return new ResponseEntity<List<RiskIndicateOption>>(list, HttpStatus.OK);
	}


}
