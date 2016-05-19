package com.guohuai.asset.manage.boot.project;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
