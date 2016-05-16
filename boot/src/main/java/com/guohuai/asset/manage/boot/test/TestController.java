package com.guohuai.asset.manage.boot.test;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.Response;

@RestController
@RequestMapping(produces = "application/json;charset=utf-8")
public class TestController extends BaseController {

	@RequestMapping(value = "/test", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<Object> test(@RequestBody TestForm form) {
		Response r = new Response();
		r.with("rows", form);
		return new ResponseEntity<Object>(r, HttpStatus.OK);
	}

	@RequestMapping(value = "/test2", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<Object> test(@RequestParam Timestamp time) {

		JSONObject j = new JSONObject();
		j.put("time", DateUtil.formatDatetime(time.getTime()));

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		for (Object obj : converter.getSupportedMediaTypes()) {
			System.out.println(obj);
		}

		return new ResponseEntity<Object>(j, HttpStatus.OK);
	}

}
