package com.guohuai.asset.manage.boot.duration.capital;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CapitalDao extends JpaRepository<CapitalEntity, String>, JpaSpecificationExecutor<CapitalEntity> {

	@Query("from CapitalEntity a where a.assetPoolOid = ?1")
	public List<CapitalEntity> findByOid(String oid);
}
