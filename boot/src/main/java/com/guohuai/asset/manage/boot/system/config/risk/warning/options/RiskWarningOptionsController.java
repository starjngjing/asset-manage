/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.system.config.risk.warning.options;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.system.config.risk.cate.RiskCateService;
import com.guohuai.asset.manage.boot.system.config.risk.options.RiskOptionsCollect;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

@RestController
@RequestMapping(value = "/ams/system/ccr/warning/options", produces = "application/json;charset=utf-8")
public class RiskWarningOptionsController extends BaseController {
	@Autowired
	RiskWarningOptionsService riskWarningOptionsService;
	@Autowired
	RiskCateService riskCateService;


	@RequestMapping(value = "/save", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskWarningOptionsResp>> save(@RequestBody RiskWarningOptionsForm form) {
		super.checkLogin();
		List<RiskWarningOptionsResp> list = this.riskWarningOptionsService.save(form);
		return new ResponseEntity<List<RiskWarningOptionsResp>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/batchDelete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<BaseResp> batchDelete(@RequestParam String indicateOid) {
		super.checkLogin();
		this.riskWarningOptionsService.batchDelete(indicateOid);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	@RequestMapping(value = "/preUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskWarningOptionsForm> preUpdate(@RequestParam String indicateOid) {
		super.checkLogin();
		RiskWarningOptionsForm form = this.riskWarningOptionsService.preUpdate(indicateOid);
		return new ResponseEntity<RiskWarningOptionsForm>(form, HttpStatus.OK);
	}

	/*
	@RequestMapping(value = "/preCollect", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskOptionsCollect>> preCollect(@RequestParam String type) {
		super.checkLogin();
		List<RiskOptionsCollect> list = this.riskOptionsService.preCollect(type);
		return new ResponseEntity<List<RiskOptionsCollect>>(list, HttpStatus.OK);
	}
	*/

	@RequestMapping(value = "/showview", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskWarningOptionsView>> showview(@RequestParam String type, @RequestParam String keyword) {
		super.checkLogin();
		List<RiskWarningOptionsView> list = this.riskWarningOptionsService.showview(type, keyword);
		return new ResponseEntity<List<RiskWarningOptionsView>>(list, HttpStatus.OK);
	}

}
