package com.guohuai.asset.manage.boot.system.config.project.warrantyExpire;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

@RestController
@RequestMapping(value = "/ams/system/ccp/warrantyExpire", produces = "application/json;charset=utf-8")
public class CCPWarrantyExpireController extends BaseController {

	@Autowired
	private CCPWarrantyExpireService ccpWarrantyExpireService;

	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<CCPWarrantyExpire> create(@Valid CCPWarrantyExpireForm form) {
		super.checkLogin();
		CCPWarrantyExpire warrantyExpire = this.ccpWarrantyExpireService.create(form);
		return new ResponseEntity<CCPWarrantyExpire>(warrantyExpire, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<CCPWarrantyExpire> update(@Valid CCPWarrantyExpireForm form) {
		super.checkLogin();
		CCPWarrantyExpire warrantyExpire = this.ccpWarrantyExpireService.update(form);
		return new ResponseEntity<CCPWarrantyExpire>(warrantyExpire, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<BaseResp> delete(@RequestParam String oid) {
		super.checkLogin();
		this.ccpWarrantyExpireService.delete(oid);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<List<CCPWarrantyExpire>> search() {
		super.checkLogin();
		List<CCPWarrantyExpire> list = this.ccpWarrantyExpireService.search();
		return new ResponseEntity<List<CCPWarrantyExpire>>(list, HttpStatus.OK);
	}

}
