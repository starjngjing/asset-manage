package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	@Autowired
	private CapitalService capitalService;

	/**
	 * 新建资产池
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/createPool", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> createPool(AssetPoolForm form) {
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
	public @ResponseBody ResponseEntity<Response> auditPool(@RequestParam String operation, @RequestParam String oid) {
		assetPoolService.auditPool(operation, oid, "STAR");
		Response r = new Response();
		r.with("result", "SUCCESSED!");
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取所有的资产池列表，支持模糊查询
	 * @return
	 */
	@RequestMapping(value = "/getAll", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAll(@RequestParam String name,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "createTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Specification<AssetPoolEntity> spec = new Specification<AssetPoolEntity>() {
			@Override
			public Predicate toPredicate(Root<AssetPoolEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get("name").as(String.class), "%" + name + "%");
			}
		};
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		List<AssetPoolForm> list = assetPoolService.getAllList(spec, pageable);
		Response r = new Response();
		r.with("rows", list);
		r.with("total", list.size());
		return new ResponseEntity<Response>(r, HttpStatus.OK);
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
		assetPoolService.editPool(form, "STAR");
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
		assetPoolService.editPoolForCash(form, "STAR");
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
	 * 获取所有资产池的资金明细
	 * @return
	 */
	@RequestMapping(value = "/getAllCapitalList", method = { RequestMethod.POST })
	public @ResponseBody ResponseEntity<Response> getAllCapitalList(String pid) {
		List<CapitalForm> list = capitalService.getCapitalListByPid(pid);
		Response r = new Response();
		r.with("rows", list);
		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}
}
