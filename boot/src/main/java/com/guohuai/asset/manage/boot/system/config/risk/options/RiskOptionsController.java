package com.guohuai.asset.manage.boot.system.config.risk.options;

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

import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

@RestController
@RequestMapping(value = "/ams/system/ccr/options", produces = "application/json;charset=utf-8")
public class RiskOptionsController extends BaseController {

	@Autowired
	private RiskOptionsService riskOptionsService;

	@RequestMapping(value = "/save", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskOptionsResp>> save(@RequestBody RiskOptionsForm form) {
		super.checkLogin();
		List<RiskOptionsResp> list = this.riskOptionsService.save(form);
		return new ResponseEntity<List<RiskOptionsResp>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/batchDelete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<BaseResp> batchDelete(@RequestParam String indicateOid) {
		super.checkLogin();
		this.riskOptionsService.batchDelete(indicateOid);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	@RequestMapping(value = "/preUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<RiskOptionsForm> preUpdate(@RequestParam String indicateOid) {
		super.checkLogin();
		RiskOptionsForm form = this.riskOptionsService.preUpdate(indicateOid);
		return new ResponseEntity<RiskOptionsForm>(form, HttpStatus.OK);
	}

	@RequestMapping(value = "/preCollect", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskOptionsCollect>> preCollect(@RequestParam String type) {
		super.checkLogin();
		List<RiskOptionsCollect> list = this.riskOptionsService.preCollect(type);
		return new ResponseEntity<List<RiskOptionsCollect>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/showview", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<List<RiskOptionsView>> showview(@RequestParam String type, @RequestParam String keyword) {
		super.checkLogin();
		List<RiskOptionsView> list = this.riskOptionsService.showview(type, keyword);
		return new ResponseEntity<List<RiskOptionsView>>(list, HttpStatus.OK);
	}

}
