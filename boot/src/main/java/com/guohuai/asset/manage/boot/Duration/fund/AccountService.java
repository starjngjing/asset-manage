package com.guohuai.asset.manage.boot.Duration.fund;

import java.util.List;

/**
 * 存续期--账户服务接口
 * @author star.zhu
 * 2016年5月16日
 */
public interface AccountService {

	/**
	 * 获取所有的出入金明细
	 * @return
	 */
	public List<AccountForm> getAllList();
	
	
}
