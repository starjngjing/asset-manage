package com.guohuai.asset.manage.boot.Duration.order;

/**
 * 存续期--订单服务接口
 * @author star.zhu
 * 2016年5月16日
 */
public interface OrderService {

	/**
	 * 货币基金（现金管理工具）申购
	 */
	public void purchaseForFund();
	
	/**
	 * 信托（计划）申购
	 */
	public void purchaseForTrust();
	
	/**
	 * 货币基金（现金管理工具）申购审核
	 */
	public void auditFund();
	
	/**
	 * 信托（计划）申购审核
	 */
	public void auditTrust();
	
	/**
	 * 货币基金（现金管理工具）资金预约
	 */
	public void cashAppointMentForFund();
	
	/**
	 * 信托（计划）资金预约
	 */
	public void cashAppointMentForTrust();
	
	/**
	 * 货币基金（现金管理工具）赎回
	 */
	public void redeem();
	
	/**
	 * 信托（计划）转让
	 */
	public void transfer();
	
	/**
	 * 出入金明细
	 */
}
