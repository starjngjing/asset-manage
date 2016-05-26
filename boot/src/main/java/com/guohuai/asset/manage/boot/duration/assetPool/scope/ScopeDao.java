package com.guohuai.asset.manage.boot.duration.assetPool.scope;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ScopeDao extends JpaRepository<ScopeEntity, String>, JpaSpecificationExecutor<ScopeEntity>  {

	@Query("from ScopeEntity a where a.assetPool.oid = ?1")
	public List<ScopeEntity> findByAssetPoolOid(String assetPoolOid);
}
