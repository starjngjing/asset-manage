package com.guohuai.asset.manage.boot.channel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.guohuai.asset.manage.component.persist.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "T_GAM_CHANNEL")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Channel extends UUID implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 微信接入方式
	 */
	public static final String CHANNEL_JOINTYPE_ftp = "ftp"; //FTP文件
	public static final String CHANNEL_JOINTYPE_api = "api"; //API接口
	
	/**
	 * 渠道启用状态
	 */
	public static final String CHANNEL_STATUS_ON = "on"; //开启
	public static final String CHANNEL_STATUS_OFF = "off"; //已停用
	
	/**
	 * 审批结果状态
	 */
	public static final String CHANNEL_APPROVESTATUS_PASS = "pass"; //通过
	public static final String CHANNEL_APPROVESTATUS_REFUSED = "refused"; //驳回
	public static final String CHANNEL_APPROVESTATUS_toApprove = "toApprove"; //待审核
	
	/**
	 * 删除状态
	 */
	public static final String CHANNEL_DELESTATUS_YES = "yes"; //已删除
	public static final String CHANNEL_DELESTATUS_NO = "no"; //正常
	
	/**
	 * 渠道编号
	 */
	private String channelCode;
	/**
	 * 渠道名称
	 */
	private String channelName;
	/**
	 * 渠道标识
	 */
	private String channelId;
	/**
	 * 渠道费率
	 */
	private String channelFee;
	/**
	 * 接入方式
	 */
	private String joinType;
	/**
	 * 合作方
	 */
	private String partner;
	/**
	 * 渠道联系人
	 */
	private String channelContactName;	
	/**
	 * 渠道联系人邮件
	 */
	private String channelEmail;
	/**
	 * 渠道联系人电话
	 */
	private String channelPhone;
	/**
	 * 渠道地址
	 */
	private String channelAddress;
	/**
	 * 渠道状态
	 */
	private String channelStatus;
	/**
	 * 审核状态
	 */
	private String approvelStatus;
	/**
	 * 删除状态
	 */
	private String deleteStatus;
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;

}
