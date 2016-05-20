package com.guohuai.asset.manage.boot.system.config.risk.indicate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/ams/system/ccr/indicate", produces = "application/json;charset=utf-8")
public class RiskIndicateController extends BaseController {

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

}
