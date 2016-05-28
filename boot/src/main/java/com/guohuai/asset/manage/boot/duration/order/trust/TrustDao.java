package com.guohuai.asset.manage.boot.duration.order.trust;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TrustDao extends JpaRepository<TrustEntity, String>, JpaSpecificationExecutor<TrustEntity> {

	@Query("from TrustEntity a where a.orderOid = ?1")
	public TrustEntity findByOrderOid(String orderOid);
	
	@Query("from TrustEntity a where a.assetPoolOid = ?1 and a.state = 0")
	public Page<TrustEntity> findByPidForConfirm(String assetPoolOid, Pageable pageable);
	
	@Query("from TrustEntity a where a.assetPoolOid = ?1 and a.state = 0")
	public List<TrustEntity> findFundListByPid(String assetPoolOid);
}
