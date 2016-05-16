package com.guohuai.asset.manage.boot.approval;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalReq {
	
	String oid;
	//项目名称
	String projectName;
	// 项目经理
	String projectManager;
	// 所属事业部
	String department;

	// 项目类型
	String pjType;

	// 项目来源
	String pjSources;

	// 城市
	String cityName;

	// 业务各类
	String businesstype;

	// 关联方
	String relatedParty;

	// 是否存量合作机构
	String isStockCoopOrg;

	// 已使用额度
	double usedAmount;

	// 目前剩余额度
	double endUseAmount;

	// 金融机构名称
	String orgName;

	// 金融产品类型1
	String financialType1;

	// 金融产品类型2
	String financialType2;

	// 金融产品是否分期发行
	String isPhasedrelease;

	// 已累计认购期数
	String subsCount;

	// 金融机构提供同业增信
	String tradeCredit;
	//测试文件上传用
	List<String> fileList;
}
