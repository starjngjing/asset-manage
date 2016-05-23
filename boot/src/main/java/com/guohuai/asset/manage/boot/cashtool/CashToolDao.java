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

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CashToolDao extends JpaRepository<CashTool, String>, JpaSpecificationExecutor<CashTool> {

	/**
	 * 根据名称模糊查询现金管理工具
	 * @Title: getCashToolByName 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param secShortName
	 * @return List<Object>    返回类型
	 */
	@Query("select ct.oid, ct.secShortName from CashTool ct where ct.secShortName like ?1")
	public List<Object> getCashToolByName(String secShortName);
}
