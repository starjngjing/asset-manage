package com.guohuai.asset.manage.boot.duration.order.fund;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FundDao extends JpaRepository<FundEntity, String>, JpaSpecificationExecutor<FundEntity> {

	@Query("from FundEntity a where a.cashTool.oid = ?1")
	public FundEntity findByCashtoolOid(String cashtoolOid);
	
	@Query("from FundEntity a where a.assetPoolOid = ?1 and a.state = 0")
	public Page<FundEntity> findByPidForConfirm(String assetPoolOid, Pageable pageable);
	
	@Query("from FundEntity a where a.assetPoolOid = ?1 and a.state = 0")
	public List<FundEntity> findFundListByPid(String assetPoolOid);
}
