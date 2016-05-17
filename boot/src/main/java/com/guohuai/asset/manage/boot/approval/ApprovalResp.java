package com.guohuai.asset.manage.boot.approval;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalResp {

	public ApprovalResp(Approval appr) {
		super();
		this.projectName = appr.getProjectName();
		this.projectManager = appr.getProjectManager();
		this.projectType = appr.getProjectType();
		this.pjSources = appr.getPjSources();
		this.projectCity = appr.getProjectCity();
		this.businesstype = appr.getBusinesstype();
		this.relatedParty = appr.getRelatedParty();
		this.isStockCoopOrg = appr.getIsStockCoopOrg();
		this.usedAmount = appr.getUsedAmount();
		this.endUseAmount = appr.getEndUseAmount();
		this.orgName = appr.getOrgName();
		this.financialType1 = appr.getFinancialType1();
		this.financialType2 = appr.getFinancialType2();
		this.isPhasedrelease = appr.getIsPhasedrelease();
		this.subsCount = appr.getSubsCount();
		this.tradeCredit = appr.getTradeCredit();
		this.state = appr.getState();
		// 记录人
		this.recordMan = appr.getRecordMan();
		// 操作人
		this.operator = appr.getOperator();

		this.contractState = appr.getContractState();
		this.createTime = appr.getCreateTime();
		this.updateTime = appr.getUpdateTime();
		this.oid = appr.getOid();
	}

	String oid;

	// 项目名称
	private String projectName;
	// 项目类型
	private String projectType;
	// 项目类型
	private String projectTypeName;
	// 项目经理
	private String projectManager;

	// 城市
	private String projectCity;

	// 资金方所在地
	private String funderAddr;

	// 资金用途
	private String fundUsage;
	// 项目还款来源
	private String payment;

	// 外部项目增信措施
	private String trustMeasures;
	// 房地产项目所在地
	private String projectAddr;
	// 开发商排名
	private String developerRank;
	// 房地产项目属性
	private String estateProp;

	// 所属事业部
	// private String department;

	// 项目来源
	private String pjSources;

	// 业务各类
	private String businesstype;

	// 关联方
	private String relatedParty;

	// 是否存量合作机构
	private String isStockCoopOrg;

	// 已使用额度
	private BigDecimal usedAmount;

	// 目前剩余额度
	private BigDecimal endUseAmount;

	// 金融机构名称
	private String orgName;

	// 金融产品类型1
	private String financialType1;

	// 金融产品类型2
	private String financialType2;

	// 金融产品是否分期发行
	private String isPhasedrelease;

	// 已累计认购期数
	private String subsCount;

	// 金融机构提供同业增信
	private String tradeCredit;

	// 状态、
	private String state;
	// 记录人
	private String recordMan;
	// 操作人
	private String operator;

	private String contractState;

	private Timestamp submitTime, updateTime, createTime;

}
