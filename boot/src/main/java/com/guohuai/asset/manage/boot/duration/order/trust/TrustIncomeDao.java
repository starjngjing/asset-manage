package com.guohuai.asset.manage.boot.duration.order.trust;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrustIncomeDao extends JpaRepository<TrustIncomeEntity, String>, JpaSpecificationExecutor<TrustIncomeEntity> {

	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_TARGET a"
	  		+ " LEFT JOIN T_GAM_ASSETPOOL_TARGET_INCOME b ON a.oid = b.targetOid"
	  		+ " WHERE a.assetPoolOid = ?1 and b.state not in ('-1', '31')", nativeQuery = true)
	public List<TrustIncomeEntity> findIncomeByPidForAppointment(String pid);
	
	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_TARGET a"
	  		+ " LEFT JOIN T_GAM_ASSETPOOL_TARGET_INCOME b ON a.oid = b.targetOid"
	  		+ " WHERE a.assetPoolOid = ?1 and b.state = 2", nativeQuery = true)
	public List<TrustIncomeEntity> findIncomeByPidForConfirm(String pid);
	
	@Query(value = "update T_GAM_ASSETPOOL_TARGET_INCOME set state = '-1' where oid = ?1", nativeQuery = true)
	@Modifying
	public void updateOrder(String oid);
	
	@Query("from TrustIncomeEntity a where a.trustEntity.oid = ?1")
	public List<TrustIncomeEntity> findSeqByPidAndOidForIncome(String oid);
}
