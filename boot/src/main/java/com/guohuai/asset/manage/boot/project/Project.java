package com.guohuai.asset.manage.boot.project;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.component.persist.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Project
 * @Description: 底层项目实体
 * @author vania
 * @date 2016年5月16日 上午10:15:56
 *
 */
@Entity
@Table(name = "T_GAM_PROJECT")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Project extends UUID {

	private static final long serialVersionUID = -8451087996991540133L;

	// 关联投资标的id
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "targetOid", referencedColumnName = "oid")
	@JsonBackReference
	private Investment investment;

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
	/**
	 * 担保方式
	 */
	@Size(max = 32, message = "保证方式oid最大32个字符！")
	private String guaranteeModeOid; // CCPWarrantyModeForm
	@Size(max = 64, message = "保证方式名称最大32个字符！")
	private String guaranteeModeTitle;
	@Digits(integer = 4, fraction = 4, message = "保证方式权重最大4位整数4位小数")
	private BigDecimal guaranteeModeWeight;
	@Size(max = 32, message = "保证方式担保期限oid最大32个字符！")
	private String guaranteeModeExpireOid; // CCPWarrantyModeForm111
	@Size(max = 64, message = "保证方式担保期限名称最大32个字符！")
	private String guaranteeModeExpireTitle;
	@Digits(integer = 4, fraction = 4, message = "保证方式担保期限权重最大4位整数4位小数")
	private BigDecimal guaranteeModeExpireWeight;
	
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
	/**
	 * 抵押方式
	 */
	@Size(max = 32, message = "抵押方式oid最大32个字符！")
	private String mortgageModeOid; // CCPWarrantyModeForm
	@Size(max = 64, message = "抵押方式名称最大32个字符！")
	private String mortgageModeTitle;
	@Digits(integer = 4, fraction = 4, message = "抵押方式权重最大4位整数4位小数")
	private BigDecimal mortgageModeWeight;
	@Size(max = 32, message = "抵押方式担保期限oid最大32个字符！")
	private String mortgageModeExpireOid; // CCPWarrantyModeForm111
	@Size(max = 64, message = "抵押方式担保期限名称最大32个字符！")
	private String mortgageModeExpireTitle;
	@Digits(integer = 4, fraction = 4, message = "抵押方式担保期限权重最大4位整数4位小数")
	private BigDecimal mortgageModeExpireWeight;
	
	/**
	 * 质押方式
	 */
	@Size(max = 32, message = "质押方式oid最大32个字符！")
	private String hypothecationModeOid; // CCPWarrantyModeForm
	@Size(max = 64, message = "质押方式名称最大32个字符！")
	private String hypothecationModeTitle;
	@Digits(integer = 4, fraction = 4, message = "质押方式权重最大4位整数4位小数")
	private BigDecimal hypothecationModeWeight;
	@Size(max = 32, message = "质押方式担保期限oid最大32个字符！")
	private String hypothecationModeExpireOid; // CCPWarrantyModeForm111
	@Size(max = 64, message = "质押方式担保期限名称最大32个字符！")
	private String hypothecationModeExpireTitle;
	@Digits(integer = 4, fraction = 4, message = "质押方式担保期限权重最大4位整数4位小数")
	private BigDecimal hypothecationModeExpireWeight;
	/**
	 * 风险系数
	 */
	private BigDecimal riskFactor;
	
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
