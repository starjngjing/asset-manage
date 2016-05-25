package com.guohuai.asset.manage.boot.duration.order.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FundDao extends JpaRepository<FundEntity, String>, JpaSpecificationExecutor<FundEntity> {

	@Query("from FundEntity a where a.cashtoolOid = ?1")
	public FundEntity findByCashtoolOid(String cashtoolOid);
}
