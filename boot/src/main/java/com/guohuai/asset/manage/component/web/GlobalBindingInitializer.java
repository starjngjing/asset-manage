package com.guohuai.asset.manage.component.web;

import java.sql.Timestamp;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

import com.guohuai.asset.manage.component.web.parameter.CustomTimestampEditor;
import com.guohuai.asset.manage.component.web.parameter.CustomerJavaDateEditor;
import com.guohuai.asset.manage.component.web.parameter.CustomerSqlDateEditor;
import com.guohuai.asset.manage.component.web.parameter.CustomerTimeEditor;

@ControllerAdvice
public class GlobalBindingInitializer {

	@InitBinder
	public void registerCustomEditors(WebDataBinder binder, WebRequest request) {

		binder.registerCustomEditor(Timestamp.class, CustomTimestampEditor.getInstance());
		binder.registerCustomEditor(java.util.Date.class, CustomerJavaDateEditor.getInstance());
		binder.registerCustomEditor(java.sql.Date.class, CustomerSqlDateEditor.getInstance());
		binder.registerCustomEditor(java.sql.Time.class, CustomerTimeEditor.getInstance());
	}

}
