/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.cashtool.pool;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CashtoolRevenueDao extends JpaRepository<CashToolRevenue, String>, JpaSpecificationExecutor<CashToolRevenue> {

	@Modifying
	@Query("delete from CashToolRevenue where dailyProfitDate=?1")
	public void deleteByDailyProfitDate(Date dailyProfitDate);
}
