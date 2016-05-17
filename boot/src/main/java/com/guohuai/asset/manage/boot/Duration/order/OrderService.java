package com.guohuai.asset.manage.boot.Duration.order;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 存续期--订单服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class OrderService {

	/**
	 * 货币基金（现金管理工具）申购
	 * @param from
	 */
	public void purchaseForFund(FundForm form) {
		
	}
	
	/**
	 * 信托（计划）申购
	 * @param from
	 */
	public void purchaseForTrust(TrustForm form) {
		
	}
	
	/**
	 * 货币基金（现金管理工具）申购审核
	 * @param oid
	 * 			标的oid
	 */
	public void auditFund(FundForm form) {
		
	}
	
	/**
	 * 信托（计划）申购审核
	 * @param oid
	 * 			标的oid
	 */
	public void auditTrust(TrustForm form) {
		
	}
	
	/**
	 * 货币基金（现金管理工具）资金预约
	 * @param oid
	 * 			标的oid
	 */
	public void cashAppointMentForFund(FundForm form) {
		
	}
	
	/**
	 * 信托（计划）资金预约
	 * @param oid
	 * 			标的oid
	 */
	public void cashAppointMentForTrust(TrustForm form) {
		
	}
	
	/**
	 * 货币基金（现金管理工具）赎回
	 * @param from
	 */
	public void redeem(FundForm from) {
		
	}
	
	/**
	 * 信托（计划）转让
	 * @param from
	 */
	public void transfer(TrustForm form) {
		
	}
	
	/**
	 * 根据资产池id获取 预约中 的信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<FundForm> getFundListForAppointmentByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 成立中 信托（计划）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<FundForm> getFundListByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 预约中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<TrustForm> getTrustListForAppointmentByPid(String pid) {
		
		return null;
	}
	
	/**
	 * 根据资产池id获取 成立中 的货币基金（现金管理工具）列表
	 * @param pid
	 * 			资产池id
	 * @return
	 */
	public List<TrustForm> getTrustListByPid(String pid) {
		
		return null;
	}
}
