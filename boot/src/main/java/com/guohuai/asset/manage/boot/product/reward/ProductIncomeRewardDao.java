package com.guohuai.asset.manage.boot.product.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductIncomeRewardDao
		extends JpaRepository<ProductIncomeReward, String>, JpaSpecificationExecutor<ProductIncomeReward> {

}
