package com.guohuai.asset.manage.boot.investment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.guohuai.asset.manage.component.persist.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 过会标的实体类
 * 
 * @author lirong
 *
 */
@Entity
@Table(name = "T_GAM_CONFERENCE_TARGET")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentMeetingAsset extends UUID implements Serializable {

	private static final long serialVersionUID = -5306960536423543505L;
	/**
	 * 投资标的
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "targetOid", referencedColumnName = "oid")
	private Investment investment;
	/**
	 * 会议
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conferenceOid", referencedColumnName = "oid")
	private InvestmentMeeting investmentMeeting;

}
