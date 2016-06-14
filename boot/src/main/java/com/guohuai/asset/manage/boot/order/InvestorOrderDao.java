package com.guohuai.asset.manage.boot.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestorOrderDao
		extends JpaRepository<InvestorOrder, String>, JpaSpecificationExecutor<InvestorOrder> {

}
