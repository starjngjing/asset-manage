package com.guohuai.asset.manage.boot.Duration.product;

import com.guohuai.asset.manage.boot.Duration.order.FundForm;
import com.guohuai.asset.manage.boot.Duration.order.TrustForm;

/**
 * 存续期--产品服务接口
 * @author star.zhu
 * 2016年5月17日
 */
public interface ProductService {

	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	public FundForm getFundByOid(String oid);

	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	public TrustForm getTrustByOid(String oid);
}
