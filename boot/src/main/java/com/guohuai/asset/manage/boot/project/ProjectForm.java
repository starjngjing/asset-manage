package com.guohuai.asset.manage.boot.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectForm implements Cloneable, Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 5787252786984515512L;
	private String oid;
	@NotNull(message = "标的id不能为空")
	private String targetOid;
	@NotNull(message = "项目名称不能为空")
	private String projectName;
	@NotNull(message = "项目类型不能为空")
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
