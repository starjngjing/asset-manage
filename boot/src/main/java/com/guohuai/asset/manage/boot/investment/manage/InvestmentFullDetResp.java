package com.guohuai.asset.manage.boot.investment.manage;

import java.util.List;

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;
import com.guohuai.asset.manage.boot.investment.meeting.VoteDetResp;
import com.guohuai.asset.manage.boot.investment.pool.TargetIncome;
import com.guohuai.asset.manage.boot.investment.pool.TargetOverdue;
import com.guohuai.asset.manage.boot.project.Project;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 全属性标的详情response
 * 
 * @author Administrator
 *
 */
@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentFullDetResp extends BaseResp {

	public InvestmentFullDetResp(Investment investment, List<Project> projects, InvestmentMeeting investmentMeeting,
			List<VoteDetResp> votes,List<TargetIncome> incomes,TargetOverdue overdue) {
		super();
		this.investment = investment;
		this.projects = projects;
		this.investmentMeeting = investmentMeeting;
		this.votes = votes;
		this.incomes = incomes;
		this.overdue = overdue;
	}

	private Investment investment; // 标的详情

	private List<Project> projects; // 底层项目详情

	private InvestmentMeeting investmentMeeting; // 过会详情

	private List<VoteDetResp> votes; // 过会表决详情
	
	/**
	 * 本息兑付信息
	 */
	private List<TargetIncome> incomes;
	
	/**
	 * 逾期信息
	 */
	private TargetOverdue overdue;
}
