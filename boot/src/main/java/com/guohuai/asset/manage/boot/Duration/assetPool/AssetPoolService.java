package com.guohuai.asset.manage.boot.Duration.assetPool;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--资产池服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class AssetPoolService {
	
	/**
	 * 新建资产池
	 */
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
	
	/**
	 * 新建审核
	 */
	public void auditPool() {
		
	}

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	public List<AssetPoolForm> getAllList() {
		
		return null;
	}
	
	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
	public List<JSONObject> getAllNameList() {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	public AssetPoolForm getById(String pid) {
		
		return null;
	}
}
