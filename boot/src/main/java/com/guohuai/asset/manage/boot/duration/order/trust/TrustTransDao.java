package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustTransDao extends JpaRepository<TrustTransEntity, String>, JpaSpecificationExecutor<TrustTransEntity> {

	@Query("from TrustTransEntity a where a.assetPoolTargetOid = ?1 and a.state < 2")
	public Page<TrustTransEntity> findTransByPidForAppointment(String pid, Pageable pageable);
	
	@Query("from TrustTransEntity a where a.assetPoolTargetOid = ?1 and a.state = 2")
	public Page<TrustTransEntity> findTransByPidForConfirm(String pid, Pageable pageable);
}
