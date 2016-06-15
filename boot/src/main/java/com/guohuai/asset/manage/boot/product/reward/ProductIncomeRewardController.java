package com.guohuai.asset.manage.boot.product.reward;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
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
import com.guohuai.asset.manage.component.web.view.PageResp;
import io.swagger.annotations.Api;

@Api("产品奖励收益率操作相关接口")
@RestController
@RequestMapping(value = "/ams/product/reward", produces = "application/json")
public class ProductIncomeRewardController extends BaseController {
	
	@Autowired
	private ProductIncomeRewardService productRewardService;
	
	/**
	 * 新加
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> savePeriodic(@Valid SaveProductRewardForm form) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp repponse = this.productRewardService.save(form, operator);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<PageResp<ProductRewardResp>> list(HttpServletRequest request, @RequestParam String productOid) {
		PageResp<ProductRewardResp> rep = this.productRewardService.list(productOid);
		return new ResponseEntity<PageResp<ProductRewardResp>>(rep, HttpStatus.OK);
	}

}
