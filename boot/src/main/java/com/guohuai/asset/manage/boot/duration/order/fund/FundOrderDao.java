package com.guohuai.asset.manage.boot.duration.order.fund;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FundOrderDao extends JpaRepository<FundOrderEntity, String>, JpaSpecificationExecutor<FundOrderEntity> {

	/*@Query("from FundOrderEntity a where a.assetPoolCashtoolOid = ?1 and a.state < 2")
	public Page<FundOrderEntity> findByPidForAppointment(String pid, Pageable pageable);*/
	
	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_CASHTOOL a"
				+ " LEFT JOIN T_GAM_ASSETPOOL_CASHTOOL_ORDER b ON a.oid = b.assetPoolCashtoolOid"
				+ " WHERE a.assetPoolOid = ?1 and b.state not in ('-1', '31') order by b.createTime desc limit ?2, ?3", nativeQuery = true)
	public List<FundOrderEntity> findByPidForAppointment(String pid, int sNO, int eNo);
	
	/*@Query("from FundOrderEntity a where a.assetPoolCashtoolOid = ?1 and a.state = 2")
	public Page<FundOrderEntity> findByPidForConfirm(String pid, Pageable pageable);*/
	
/*	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_CASHTOOL a"
			+ " LEFT JOIN T_GAM_ASSETPOOL_CASHTOOL_ORDER b ON a.oid = b.assetPoolCashtoolOid"
			+ " WHERE a.assetPoolOid = ?1 and b.state in (-1,0,1) limit ?2, ?3", nativeQuery = true)
	public List<FundOrderEntity> findByPidForConfirm(String pid, int sNO, int eNo);*/
	
	@Query(value = "update T_GAM_ASSETPOOL_CASHTOOL_ORDER set state = '-1' where oid = ?1 and operator = ?2", nativeQuery = true)
	@Modifying
	public void updateOrder(String oid, String operator);
}
