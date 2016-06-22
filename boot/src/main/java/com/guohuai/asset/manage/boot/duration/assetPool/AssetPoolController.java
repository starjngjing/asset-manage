package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.boot.duration.capital.CapitalForm;
import com.guohuai.asset.manage.boot.duration.capital.CapitalService;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.PageResp;
import com.guohuai.asset.manage.component.web.view.Response;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;


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
	@Autowired
	private CapitalService capitalService;

	/**
	 * 新建资产池
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/createPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> createPool(AssetPoolForm form) {
		assetPoolService.createPool(form, super.getLoginAdmin());
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
	public @ResponseBody ResponseEntity<Response> auditPool(@RequestParam String operation, @RequestParam String oid) {
		assetPoolService.auditPool(operation, oid, super.getLoginAdmin());
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取所有的资产池列表，支持模糊查询
	 * @return
	 */
	@RequestMapping(value = "/getAll", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<PageResp<AssetPoolForm>> getAll(
			@And({
				@Spec(params = "name", path = "name", spec = Like.class),
				@Spec(params = "state", path = "state", spec = Equal.class)
			})Specification<AssetPoolEntity> spec,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		PageResp<AssetPoolForm> rep = assetPoolService.getAllList(spec, pageable);
		return new ResponseEntity<PageResp<AssetPoolForm>>(rep, HttpStatus.OK);
	}

	/**
	 * 根据oid获取资产池的详细信息
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "/getPoolByOid", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getPoolByOid(@RequestParam String oid) {
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
	public @ResponseBody ResponseEntity<Response> editPool(AssetPoolForm form) {
		assetPoolService.editPool(form, super.getLoginAdmin());
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 编辑资产池账户信息
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/editPoolForCash", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> editPoolForCash(AssetPoolForm form) {
		assetPoolService.editPoolForCash(form, super.getLoginAdmin());
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 逻辑删除资产池
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/updateAssetPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> updateAssetPool(String pid) {
		assetPoolService.updateAssetPool(pid);
		Response r = new Response();
		r.with("result", "SUCCESSED!");
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

	/**
	 * 计算资产池每日收益
	 * @return
	 */
	@RequestMapping(value = "/calcPoolProfit", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> calcPoolProfit() {
		assetPoolService.calcPoolProfit();
		Response r = new Response();
		r.with("result", "SUCCESS");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取所有资产池的资金明细
	 * @return
	 */
	@RequestMapping(value = "/getAllCapitalList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<PageResp<CapitalForm>> getAllCapitalList(String pid,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		pid = assetPoolService.getPid(pid);
		PageResp<CapitalForm> rep = capitalService.getCapitalListByPid(pid, pageable);
		return new ResponseEntity<PageResp<CapitalForm>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 手动触发每日收益计算
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/userPoolProfit", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> calcCapital(@RequestParam String oid, @RequestParam String type) {
		assetPoolService.userPoolProfit(oid, super.getLoginAdmin(), type);
		Response r = new Response();
		r.with("result", "SUCCESS");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
	
	/**
	 * 更新资产池的偏离损益
	 * @param form
	 */
	@RequestMapping(value = "/updateDeviationValue", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> updateDeviationValue(AssetPoolForm form) {
		assetPoolService.updateDeviationValue(form, super.getLoginAdmin());
		Response r = new Response();
		r.with("result", "SUCCESS");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
