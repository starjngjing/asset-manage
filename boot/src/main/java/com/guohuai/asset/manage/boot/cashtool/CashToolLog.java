/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.cashtool;

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
 * <p>Title: CashToolLog.java</p>    
 * <p>现金管理工具变动记录 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午1:36:16   
 */   
@Entity
@Table(name = "T_GAM_CASHTOOL_LOG")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CashToolLog extends UUID {

	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = -4813450343295373852L;
	
	/**
	 * 关联现金管理工具
	 */
	private CashTool cashtool;
	/**
	 * 发生时间
	 */
	private Timestamp eventTime;
	/**
	 * 操作员ID
	 */
	private String operator;
	/**
	 * 事件类型
	 */
	private String eventType;
}
