/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.investment.pool;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>Title: UnEstablishForm.java</p>    
 * <p>标的不成立表单 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午7:37:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnEstablishForm implements Serializable {

	/**
	 * @Fields serialVersionUID : 
	 */

	private static final long serialVersionUID = 2246250729662434487L;
	@NotNull(message = "投资标的id不能为空")
	private String oid;
	/**
	 * 募集期收益
	 */
	@NotNull(message = "投资标的募集期收益不能为空")
	@Digits(integer = 10, fraction = 4, message = "投资标的募集期收益最大10位整数4位小数")
	private BigDecimal collectIncomeRate;

	// @NotNull(message = "投资标的收益截止日期不能为空")
	// private Timestamp arorFirstDate;
	
	/**
	 * 操作员
	 */
	@Size(max = 32, message = "操作人最大32个字符！")
	private String operator;
}
