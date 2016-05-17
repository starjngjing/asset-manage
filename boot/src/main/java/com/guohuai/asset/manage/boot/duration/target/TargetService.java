package com.guohuai.asset.manage.boot.duration.target;

import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.duration.order.FundForm;
import com.guohuai.asset.manage.boot.duration.order.TrustForm;

/**
 * 存续期--产品服务接口
 * @author star.zhu
 * 2016年5月17日
 */
@Service
public class TargetService {

	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	public FundForm getFundByOid(String oid) {
		
		return null;
	}

	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	public TrustForm getTrustByOid(String oid) {
		
		return null;
	}
}
