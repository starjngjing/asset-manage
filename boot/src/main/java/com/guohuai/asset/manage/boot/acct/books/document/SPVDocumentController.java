package com.guohuai.asset.manage.boot.acct.books.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;

@RestController
@RequestMapping(value = "/ams/acct/books/document/spv", produces = "application/json;charset=utf-8")
public class SPVDocumentController extends BaseController {

	@Autowired
	private SPVDocumentService spvDocumentService;

	@RequestMapping(value = "/purchase", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<DocumentResp> purchase(DocumentForm form) {
		super.checkLogin();
		Document d = this.spvDocumentService.purchase(form.getRelative(), form.getTicket(), form.getMarket(), form.getSaleable(), form.getChargefee());
		return new ResponseEntity<DocumentResp>(new DocumentResp(d), HttpStatus.OK);
	}

	@RequestMapping(value = "/redemption", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<DocumentResp> redemption(DocumentForm form) {
		super.checkLogin();
		Document d = this.spvDocumentService.redemption(form.getRelative(), form.getTicket(), form.getMarket(), form.getSaleable(), form.getChargefee());
		return new ResponseEntity<DocumentResp>(new DocumentResp(d), HttpStatus.OK);
	}

}
