package com.guohuai.asset.manage.boot.duration.order.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundRedeemDao extends JpaRepository<FundRedeemEntity, String>, JpaSpecificationExecutor<FundRedeemEntity> {

	public FundRedeemEntity findByOid(String oid);
}
