package com.guohuai.asset.manage.boot.duration.order.fund;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FundOrderDao extends JpaRepository<FundOrderEntity, String>, JpaSpecificationExecutor<FundOrderEntity> {

	public FundOrderEntity findByOid(String oid);
	
	@Query("from FundOrderEntity a where a.cashToolEntity.oid = ?1 and a.state < 1")
	public List<FundOrderEntity> findByPidForAppointment(String pid);
}
