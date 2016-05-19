package com.guohuai.asset.manage.boot.project;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResp {

	public ProjectResp(Project appr) {
		BeanUtils.copyProperties(appr, this);
	}

	private String oid;
	private String targetOid;
	private String projectName;
	private String projectType;
	private String projectTypeName;
	private String projectManager;
	private String projectCity;
	private String funderAddr;
	private String fundUsage;
	private String payment;
	private String trustMeasures;
	private String trustMeasuresName;
	private String projectAddr;
	private String developerRank;
	private String estateProp;
	private String estatePropName;
	private String estateCompletion;
	private String govLevel;
	private String warrantor;
	private String warrantorAddr;
	private BigDecimal warrantorCapital;
	private BigDecimal warrantorDebt;
	private String pledge;
	private String mortgager;
	private String pledgor;
	private String pledgeAddr;
	private String pledgeType;
	private String pledgeWeight;
	private String pledgeName;
	private String pledgeAssessment;
	private BigDecimal pledgeValuation;
	private String pledgePriority;
	private BigDecimal pledgeRatio;
	private BigDecimal margin;
	private BigDecimal projectScale;
	private BigDecimal financeCosts;
	private String spv;
	private BigDecimal spvTariff;
	private String projectGrade;
	private String gradeAssessment;
	private Timestamp gradeTime;
	private String creator;
	private String operator;
	private Timestamp createTime;
	private Timestamp updateTime;

}
