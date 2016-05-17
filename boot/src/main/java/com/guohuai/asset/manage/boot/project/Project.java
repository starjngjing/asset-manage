package com.guohuai.asset.manage.boot.project;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

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
	private String targetOid;
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

	// @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "approval")
	// @BatchSize (size = 5)
	// private List <ApprovalRisk> orders = new ArrayList <ApprovalRisk> ();
}
