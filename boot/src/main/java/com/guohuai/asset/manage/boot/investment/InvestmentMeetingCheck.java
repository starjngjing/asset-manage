package com.guohuai.asset.manage.boot.investment;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.component.persist.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 过会检查项实体类
 * 
 * @author lirong
 *
 */
@Entity
@Table(name = "T_GAM_INVESTMENT_MEETING_CHECK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentMeetingCheck extends UUID implements Serializable {

	private static final long serialVersionUID = -4023827249470662421L;

	/**
	 * 过会检查项状态 check 已检查 notcheck 未检查
	 * 
	 */
	public static final String MEETINGCHEC_STATUS_check = "check";
	public static final String MEETINGCHEC_STATUS_notcheck = "notcheck";

	/**
	 * 投资标的
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "investmentOid", referencedColumnName = "oid")
	private Investment investment;
	/**
	 * 会议
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meetingOid", referencedColumnName = "oid")
	private InvestmentMeeting InvestmentMeeting;
	/**
	 * 检查项名称
	 */
	private String checkTitle;
	/**
	 * 检查状态
	 */
	private String status;
	/**
	 * 检查成功说明
	 */
	private String checkSucDet;
	/**
	 * 检查成功说明
	 */
	private String checkSucEnclosure;
	/**
	 * 检查人
	 */
	private String checkUser;
	/**
	 * 检查时间
	 */
	private Timestamp checkTime;

}
