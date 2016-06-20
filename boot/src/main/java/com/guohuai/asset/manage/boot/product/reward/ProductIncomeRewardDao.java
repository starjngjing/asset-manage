package com.guohuai.asset.manage.boot.product.reward;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductIncomeRewardDao extends JpaRepository<ProductIncomeReward, String>, JpaSpecificationExecutor<ProductIncomeReward> {

	@Query(value = "from ProductIncomeReward pir right join fetch pir.product where pir.product.oid in ?1")
	public List<ProductIncomeReward> findByProductOid(List<String> poids);
}
