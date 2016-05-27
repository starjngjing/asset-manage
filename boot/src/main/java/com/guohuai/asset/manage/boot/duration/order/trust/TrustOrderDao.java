package com.guohuai.asset.manage.boot.duration.order.trust;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustOrderDao extends JpaRepository<TrustOrderEntity, String>, JpaSpecificationExecutor<TrustOrderEntity> {

	@Query("from TrustOrderEntity a where a.targetOid = ?1 and a.state < 2")
	public List<TrustOrderEntity> findPurchaseByPidForAppointment(String pid);
	
	@Query("from TrustOrderEntity a where a.targetOid = ?1 and a.state = 2")
	public List<TrustOrderEntity> findPurchaseByPidForConfirm(String pid);
}
