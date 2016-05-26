package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustOrderDao extends JpaRepository<TrustOrderEntity, String>, JpaSpecificationExecutor<TrustOrderEntity> {

	@Query("from TrustOrderEntity a where a.targetOid = ?1 and a.state < 2")
	public Page<TrustOrderEntity> findPurchaseByPidForAppointment(String pid, Pageable pageable);
	
	@Query("from TrustOrderEntity a where a.targetOid = ?1 and a.state = 2")
	public Page<TrustOrderEntity> findPurchaseByPidForConfirm(String pid, Pageable pageable);
}
