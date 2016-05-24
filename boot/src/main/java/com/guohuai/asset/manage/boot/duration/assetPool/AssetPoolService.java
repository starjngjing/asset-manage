package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
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
	
	/**
	 * 新建资产池
	 * @param form
	 * @param uid
	 */
	public void createPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = new AssetPoolEntity();
		try {
			entity.setOid(StringUtil.uuid());
			BeanUtils.copyProperties(entity, form);
			entity.setState("未成立");
			entity.setCreater(uid);
			entity.setCreateTime(DateUtil.getSqlCurrentDate());
			
			assetPoolDao.save(entity);
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
	public void auditPool(String operation, String oid, String uid) {
		if ("yes".equals(operation)) {
			AssetPoolEntity entity = assetPoolDao.findOne(oid);
			entity.setState("成立");
		}
	}

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	public List<AssetPoolForm> getAllList() {
		List<AssetPoolForm> formList = Lists.newArrayList();
		List<AssetPoolEntity> entityList = assetPoolDao.findAll();
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
	
	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
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
	public AssetPoolForm getById(String pid) {
		AssetPoolForm form = new AssetPoolForm();
		AssetPoolEntity entity = assetPoolDao.findOne(pid);
		try {
			BeanUtils.copyProperties(form, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return form;
	}
}
