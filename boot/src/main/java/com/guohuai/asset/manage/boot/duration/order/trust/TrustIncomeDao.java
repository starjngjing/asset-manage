package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustIncomeDao extends JpaRepository<TrustIncomeEntity, String>, JpaSpecificationExecutor<TrustIncomeEntity> {

	@Query("from TrustIncomeEntity a where a.assetPoolTargetOid = ?1 and a.state < 2")
	public Page<TrustIncomeEntity> findIncomeByPidForAppointment(String pid, Pageable pageable);
	
	@Query("from TrustIncomeEntity a where a.assetPoolTargetOid = ?1 and a.state = 2")
	public Page<TrustIncomeEntity> findIncomeByPidForConfirm(String pid, Pageable pageable);
}
