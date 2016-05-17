package com.guohuai.asset.manage.boot.Duration.assetPool.impl;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.boot.Duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.Duration.assetPool.AssetPoolForm;
import com.guohuai.asset.manage.boot.Duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.component.util.StringUtil;

public class AssetPoolServiceImpl implements AssetPoolService {

	@Override
	public void createPool(AssetPoolForm form) {
		AssetPoolEntity entity = new AssetPoolEntity();
		try {
			entity.setOid(StringUtil.uuid());
			BeanUtils.copyProperties(entity, form);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void auditPool() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<AssetPoolForm> getAllList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JSONObject> getAllNameList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetPoolForm getById(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

}
