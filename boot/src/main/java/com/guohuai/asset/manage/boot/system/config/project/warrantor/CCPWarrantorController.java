package com.guohuai.asset.manage.boot.system.config.project.warrantor;

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
@RequestMapping(value = "/ams/system/ccp/warrantor", produces = "application/json;charset=utf-8")
public class CCPWarrantorController extends BaseController {

	@Autowired
	private CCPWarrantorService ccpWarrantorService;

	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<CCPWarrantor> create(@Valid CCPWarrantorForm form) {
		super.checkLogin();
		CCPWarrantor warrantor = this.ccpWarrantorService.create(form);
		return new ResponseEntity<CCPWarrantor>(warrantor, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<CCPWarrantor> update(@Valid CCPWarrantorForm form) {
		super.checkLogin();
		CCPWarrantor warrantor = this.ccpWarrantorService.update(form);
		return new ResponseEntity<CCPWarrantor>(warrantor, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<BaseResp> delete(@RequestParam String oid) {
		super.checkLogin();
		this.ccpWarrantorService.delete(oid);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<List<CCPWarrantor>> search() {
		super.checkLogin();
		List<CCPWarrantor> list = this.ccpWarrantorService.search();
		return new ResponseEntity<List<CCPWarrantor>>(list, HttpStatus.OK);
	}

}
