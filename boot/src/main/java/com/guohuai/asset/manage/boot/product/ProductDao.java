package com.guohuai.asset.manage.boot.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductDao extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
	
	/**
	 * 根据产品编号获取产品实体
	 * @param productCode
	 * @return {@link Product}
	 */
	public Product findByCode(String code);
	
	public List<Product> findByOidIn(List<String> oids);
	
}
