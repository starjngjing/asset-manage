package com.guohuai.asset.manage.boot.Duration.assetPool;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 存续期--资产池服务接口
 * @author star.zhu
 * 2016年5月16日
 */
public interface AssetPoolService {
	
	/**
	 * 新建资产池
	 */
	public void createPool(AssetPoolForm form);
	
	/**
	 * 新建审核
	 */
	public void auditPool();

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	public List<AssetPoolForm> getAllList();
	
	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
	public List<JSONObject> getAllNameList();
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	public AssetPoolForm getById(String pid);
}
