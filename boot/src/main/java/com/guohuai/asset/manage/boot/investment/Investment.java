package com.guohuai.asset.manage.boot.investment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
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
 * 投资标的实体类
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "T_GAM_TARGET")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Investment extends UUID implements Serializable {

	private static final long serialVersionUID = 7108314017534718240L;

	/**
	 * 投资标的状态status waitPretrial 等待预审 pretrial 预审中 waitMeeting 待过会 metting 过会中
	 * collecting 募集期 （运营期） reject 驳回 invalid 作废 
	 */
	public static final String INVESTMENT_STATUS_waitPretrial = "waitPretrial";
	public static final String INVESTMENT_STATUS_pretrial = "pretrial";
	public static final String INVESTMENT_STATUS_waitMeeting = "waitMeeting";
	public static final String INVESTMENT_STATUS_metting = "metting";
	public static final String INVESTMENT_STATUS_collecting = "collecting";
	public static final String INVESTMENT_STATUS_establish = "establish"; // 成立
	public static final String INVESTMENT_STATUS_unEstablish = "unEstablish"; //成立失败
	public static final String INVESTMENT_STATUS_reject = "reject"; //  reject 驳回 
	public static final String INVESTMENT_STATUS_overdue = "overdue"; // overdue逾期
	public static final String INVESTMENT_STATUS_invalid = "invalid"; //invalid 作废 

	/**
	 * 标的编号
	 */
	private String sn;
	/**
	 * 表单名称
	 */
	private String name;
	/**
	 * 付息方式
	 */
	private String accrualType;
	/**
	 * 拟成立日
	 */
	private Date expSetDate;
	/**
	 * 成立日
	 */
	private Date setDate;
	/**
	 * 标的类型
	 */
	private String type;
	/**
	 * 投资方向
	 */
	private String investment;
	/**
	 * 预计收益
	 */
	private BigDecimal expIncome;
	/**
	 * 标的规模
	 */
	private BigDecimal raiseScope;
	/**
	 * 标的限期
	 */
	private Integer lift;
	/**
	 * 标的限期单位
	 */
	private String lifeUnit;
	/**
	 * 标的限期（日）
	 */
	private Integer lifed;
	/**
	 * 合同年天数
	 */
	private Integer contractDays;
	/**
	 * 起购金额
	 */
	private BigDecimal floorVolume;
	/**
	 * 收益说明
	 */
	private String expArorDesc;
	/**
	 * 付息周期方式
	 */
	private String accrualCycleType;
	/**
	 * 预计年化收益
	 */
	private BigDecimal expAror;
	/**
	 * 预计年化收益区间
	 */
	private String expArorSec;
	/**
	 * 主题评级
	 */
	private String subjectRating;
	/**
	 * 评级机构
	 */
	private String ratingAgency;
	/**
	 * 评级时间
	 */
	private Timestamp ratingTime;
	/**
	 * 内部评级
	 */
	private String irb;
	/**
	 * 优先级
	 */
	private String prior;
	/**
	 * 募集起始日
	 */
	private Date collectStartDate;
	/**
	 * 募集截止日
	 */
	private Date collectEndDate;
	/**
	 * 目前优势
	 */
	private String collectIncomeRate;
	/**
	 * 融资方
	 */
	private String financer;
	/**
	 * 融资方简介
	 */
	private String financerDesc;
	/**
	 * 担保方
	 */
	private String warrantor;
	/**
	 * 担保方简介
	 */
	private String warrantorDesc;
	/**
	 * 资金用途
	 */
	private String usages;
	/**
	 * 还款说明
	 */
	private String repayment;
	/**
	 * 风控措施
	 */
	private String risk;
	/**
	 * 首付息日
	 */
	private Date arorFirstDate;
	/**
	 * 付息日
	 */
	private Integer accrualDate;
	/**
	 * 资产类型名称
	 */
	private String typeName;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 操作员
	 */
	private String operator;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;

}
