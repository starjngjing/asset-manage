package com.guohuai.asset.manage.boot.duration.order.trust;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustIncomeDao extends JpaRepository<TrustIncomeEntity, String>, JpaSpecificationExecutor<TrustIncomeEntity> {

	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_TARGET a"
	  		+ " LEFT JOIN T_GAM_ASSETPOOL_TARGET_INCOME b ON a.oid = b.assetPoolTargetOid"
	  		+ " WHERE a.targetOid = ?1 and a.state < 2", nativeQuery = true)
	public List<TrustIncomeEntity> findIncomeByPidForAppointment(String pid);
	
	@Query(value = "SELECT b.* FROM T_GAM_ASSETPOOL_TARGET a"
	  		+ " LEFT JOIN T_GAM_ASSETPOOL_TARGET_INCOME b ON a.oid = b.assetPoolTargetOid"
	  		+ " WHERE a.targetOid = ?1 and a.state = 2", nativeQuery = true)
	public List<TrustIncomeEntity> findIncomeByPidForConfirm(String pid);
}
