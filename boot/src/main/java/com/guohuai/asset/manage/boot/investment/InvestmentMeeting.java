package com.guohuai.asset.manage.boot.investment;

import java.io.Serializable;
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
 * 投资标的过会-会议实体类
 * 
 * @author Administrator
 *
 */

@Entity
@Table(name = "T_GAM_CONFERENCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InvestmentMeeting extends UUID implements Serializable {

	private static final long serialVersionUID = -248787174503909861L;
	private String sn;
	private String title;
	private String tate;
	private Timestamp conferenceTime;
	private String creator;
	private String operator;
	private Timestamp createTime;
	private Timestamp updateTime;
}
