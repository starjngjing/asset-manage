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
 * 过会表决实体类
 * 
 * @author lirong
 *
 */
@Entity
@Table(name = "T_GAM_INVESTMENT_MEETING_VOTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentMeetingVote extends UUID implements Serializable {

	private static final long serialVersionUID = -5306960536423543505L;

	/**
	 * 投票状态 notvote 为投票 approve 赞成 notapprove 不赞成 notpass 一票否决
	 * 
	 */
	public static final String VOTE_STATUS_notvote = "notvote";
	public static final String VOTE_STATUS_approve = "approve";
	public static final String VOTE_STATUS_notapprove = "notapprove";
	public static final String VOTE_STATUS_notpass = "notpass";

	/**
	 * 投资标的
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "investmentOid", referencedColumnName = "oid")
	private Investment investment;
	/**
	 * 参会人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userOid", referencedColumnName = "oid")
	private InvestmentMeetingUser investmentMeetingUser;
	/**
	 * 投票状态
	 */
	private String voteStatus;
	/**
	 * 投票时间
	 */
	private Timestamp voteTime;
	/**
	 * 备注
	 */
	private String voteRemarks;
	/**
	 * 附件
	 */
	private String enclosure;

}
