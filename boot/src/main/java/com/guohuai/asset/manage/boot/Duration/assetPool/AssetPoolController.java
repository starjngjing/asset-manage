package com.guohuai.asset.manage.boot.Duration.assetPool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.Response;


/**
 * 存续期--资产池操作入口
 * @author star.zhu
 * 2016年5月16日
 */
@Controller
public class AssetPoolController extends BaseController {
	
	@Autowired
	private AssetPoolService assetPoolService;

	/**
	 * 获取所有的资产池列表
	 * @return
	 */
	@RequestMapping(value = "/getAll", method = { RequestMethod.GET})
	public @ResponseBody ResponseEntity<Response> getAll() {
		List<AssetPoolForm> list = assetPoolService.getAllList();
		Response r = new Response();
		r.with("result", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
