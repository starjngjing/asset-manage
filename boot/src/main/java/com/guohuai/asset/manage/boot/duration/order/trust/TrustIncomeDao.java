package com.guohuai.asset.manage.boot.duration.order.trust;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrustIncomeDao extends JpaRepository<TrustIncomeEntity, String>, JpaSpecificationExecutor<TrustIncomeEntity> {

	public TrustIncomeEntity findByOid(String oid);
}
