package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.duration.assetPool.scope.ScopeService;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--资产池服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class AssetPoolService {
	
	@Autowired
	private AssetPoolDao assetPoolDao;
	
	@Autowired
	private ScopeService scopeService;
	
	/**
	 * 新建资产池
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void createPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = new AssetPoolEntity();
		try {
			BeanUtils.copyProperties(entity, form);
			entity.setOid(StringUtil.uuid());
			entity.setCashPosition(form.getScale());
			entity.setState("未审核");
			entity.setCreater(uid);
			entity.setCreateTime(DateUtil.getSqlCurrentDate());
			
			assetPoolDao.save(entity);
			
			for (String s : form.getScopes()) {
				scopeService.save(entity, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新建审核
	 * @param operation
	 * 				yes：同意
	 * 				no：不同意
	 * @param oid
	 * @param uid
	 */
	@Transactional
	public void auditPool(String operation, String oid, String uid) {
		AssetPoolEntity entity = assetPoolDao.findOne(oid);
		if ("yes".equals(operation)) {
			entity.setState("成立");
		} else {
			entity.setState("未通过");
		}
	}

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	@Transactional
	public List<AssetPoolForm> getAllList(Specification<AssetPoolEntity> spec, Pageable pageable) {
		List<AssetPoolForm> formList = Lists.newArrayList();
		Page<AssetPoolEntity> entityList = assetPoolDao.findAll(spec, pageable);
		AssetPoolForm form = null;
		if (null != entityList.getContent() && entityList.getContent().size() > 0) {
			for (AssetPoolEntity entity : entityList.getContent()) {
				form = new AssetPoolForm();
				try {
					BeanUtils.copyProperties(form, entity);
					form.setState(entity.getState().equals("未审核") ?  "0" : entity.getState().equals("成立") ? "1" : "-1");
				} catch (Exception e) {
					e.printStackTrace();
				}
				formList.add(form);
			}
		}
		
		return formList;
	}
	
	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
	@Transactional
	public List<JSONObject> getAllNameList() {
		List<JSONObject> jsonObjList = Lists.newArrayList();
		List<Object> objList = assetPoolDao.findAllNameList();
		if (!objList.isEmpty()) {
			Object[] obs = null;
			JSONObject jsonObj = null;
			for (Object obj : objList) {
				obs = (Object[]) obj;
				jsonObj = new JSONObject();
				jsonObj.put("oid", obs[0]);
				jsonObj.put("name", obs[1]);
				
				jsonObjList.add(jsonObj);
			}
		}
		
		return jsonObjList;
	}
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	@Transactional
	public AssetPoolEntity getByOid(String pid) {
		AssetPoolEntity entity = assetPoolDao.findOne(pid);
		
		return entity;
	}
	
	/**
	 * 保存
	 * @param entity
	 */
	@Transactional
	public void save(AssetPoolEntity entity) {
		assetPoolDao.save(entity);
	}
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	@Transactional
	public AssetPoolForm getPoolByOid(String pid) {
		AssetPoolForm form = new AssetPoolForm();
		AssetPoolEntity entity = this.getByOid(pid);
		String[] scopes = scopeService.getScopes(pid);
		try {
			BeanUtils.copyProperties(form, entity);
			form.setScopes(scopes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return form;
	}
	
	/**
	 * 编辑资产池
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void editPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = assetPoolDao.findOne(form.getOid());
		try {
			BeanUtils.copyProperties(entity, form);
			entity.setCashPosition(form.getScale());
			entity.setState("未审核");
			entity.setCreater(uid);
			entity.setCreateTime(DateUtil.getSqlCurrentDate());
			
			assetPoolDao.save(entity);
			
			for (String s : form.getScopes()) {
				scopeService.save(entity, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有资产池列表
	 * @param name
	 * @return
	 */
	@Transactional
	public List<AssetPoolForm> getListByName(String name) {
		List<AssetPoolForm> formList = Lists.newArrayList();
		List<AssetPoolEntity> entityList = assetPoolDao.getListByName(name);
		if (!entityList.isEmpty()) {
			AssetPoolForm form = null;
			for (AssetPoolEntity entity : entityList) {
				form = new AssetPoolForm();
				try {
					BeanUtils.copyProperties(form, entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				formList.add(form);
			}
		}
		
		return formList;
	}
}
