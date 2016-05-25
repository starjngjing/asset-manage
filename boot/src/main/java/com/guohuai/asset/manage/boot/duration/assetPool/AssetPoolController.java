package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.Response;


/**
 * 存续期--资产池操作入口
 * @author star.zhu
 * 2016年5月16日
 */
@RestController
@RequestMapping(value = "/ams/duration/assetPool", produces = "application/json;charset=utf-8")
public class AssetPoolController extends BaseController {
	
	@Autowired
	private AssetPoolService assetPoolService;

	/**
	 * 新建资产池
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/createPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> createPool(@RequestBody AssetPoolForm form) {
		assetPoolService.createPool(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 新建审核
	 * @param operation
	 * 			操作：yes（通过）；no（不通过）
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/auditPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> auditPool(String operation, String oid) {
		assetPoolService.auditPool(operation, oid, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取所有的资产池列表
	 * @return
	 */
	@RequestMapping(value = "/getAll", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAll() {
		List<AssetPoolForm> list = assetPoolService.getAllList();
		Response r = new Response();
		r.with("rows", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 根据oid获取资产池的详细信息
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/getPoolByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getPoolByOid(String oid) {
		AssetPoolForm form = assetPoolService.getPoolByOid(oid);
		Response r = new Response();
		r.with("result", form);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 编辑资产池
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/editPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> editPool(@RequestBody AssetPoolForm form) {
		assetPoolService.editPool(form, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 根据名称获取所有的资产池列表，支持模糊查询
	 * @return
	 */
	@RequestMapping(value = "/getListByName", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getListByName(String name) {
		List<AssetPoolForm> list = assetPoolService.getAllList();
		Response r = new Response();
		r.with("rows", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
	@RequestMapping(value = "/getAllNameList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAllNameList() {
		List<JSONObject> jsonList = assetPoolService.getAllNameList();
		Response r = new Response();
		r.with("rows", jsonList);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
