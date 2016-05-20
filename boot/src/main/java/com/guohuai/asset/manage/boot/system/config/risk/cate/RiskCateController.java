package com.guohuai.asset.manage.boot.system.config.risk.cate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/ams/system/ccr/cate", produces = "application/json;charset=utf-8")
public class RiskCateController extends BaseController {

	@Autowired
	private RiskCateService riskCateService;

	@RequestMapping(value = "/options", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<Object> options(@RequestParam(required = false) String type) {
		super.checkLogin();

		if (StringUtil.isEmpty(type)) {
			Map<String, List<RiskCate>> map = this.riskCateService.options();
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} else {
			List<RiskCate> list = this.riskCateService.options(type);
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		}

	}

}
