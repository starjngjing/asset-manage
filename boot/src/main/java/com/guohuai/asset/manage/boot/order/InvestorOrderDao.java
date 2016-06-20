package com.guohuai.asset.manage.boot.order;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface InvestorOrderDao
		extends JpaRepository<InvestorOrder, String>, JpaSpecificationExecutor<InvestorOrder> {

	/**
	 * 查询满足条件的订单列表，计算资产池的实际市值
	 * @param pid
	 * 			资产池id
	 * @param baseDate
	 * 			基准日
	 * @author star
	 * @return
	 */
	@Query(value = "SELECT c.* FROM T_GAM_INVESTOR_ACCOUNT a "
			+ " INNER JOIN T_GAM_INVESTOR b ON a.investorOid = b.oid AND b.type = 'SPV'"
			+ " LEFT JOIN T_GAM_INVESTOR_ORDER c ON a.oid = c.accountOid" 
			+ " WHERE a.assetpoolOid = ?1"
			+ " AND c.orderType IN ('INVEST','REDEEM')"
			+ " AND c.orderStatus = 'CONFIRM'"
			+ " AND c.entryStatus = 'NO'"
			+ " AND c.orderDate <= ?2", nativeQuery = true)
	public List<InvestorOrder> getListForMarketAdjust(String pid, Date baseDate);
}
