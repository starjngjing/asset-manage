package com.guohuai.asset.manage.boot.investment.meeting;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 标的过会投票结果返回类
 * 
 * @author Administrator
 *
 */
@Data
public class InvestmentVoteDetResp {

	/**
	 * 角色
	 */
	private String roll;
	/**
	 * 投票状态
	 */
	private String state;
	/**
	 * 投票人
	 */
	private String name;
	/**
	 * 投票时间
	 */
	private Timestamp time;
}
