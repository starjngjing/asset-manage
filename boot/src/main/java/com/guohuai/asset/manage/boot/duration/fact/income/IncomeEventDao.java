package com.guohuai.asset.manage.boot.duration.fact.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncomeEventDao extends JpaRepository<IncomeEvent, String>, JpaSpecificationExecutor<IncomeEvent> {

}
