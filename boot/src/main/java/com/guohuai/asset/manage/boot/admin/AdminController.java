package com.guohuai.asset.manage.boot.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/platform/amp/admin", produces = "application/json;charset=utf-8")
public class AdminController extends BaseController {

	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<AdminListResp> search() {
		List<Admin> list = adminService.list();
		AdminListResp r = new AdminListResp(list);
		return new ResponseEntity<AdminListResp>(r, HttpStatus.OK);
	}

}
