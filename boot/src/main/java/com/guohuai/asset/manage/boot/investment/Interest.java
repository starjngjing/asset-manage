/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.investment;

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
 * 本息兑付实体
 * <p>Title: Interest.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 上午11:35:30   
 */   
@Entity
@Table(name = "T_GAM_INTEREST")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Interest extends UUID {

	private static final long serialVersionUID = 4793726996152893232L;

	/**
	 * 关联投资标的
	 */
	private String targetOid;

	/**
	 * 兑付期数
	 */
	private Integer seq;

	/**
	 * 实际收益
	 */
	private BigDecimal incomeRate;

	/**
	 * 收益支付日
	 */
	private Timestamp incomeDate;

	/**
	 * 操作员
	 */
	private String operator;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 修改时间
	 */
	private Timestamp updateTime;

}