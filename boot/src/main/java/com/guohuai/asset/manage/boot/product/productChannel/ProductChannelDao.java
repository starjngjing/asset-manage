package com.guohuai.asset.manage.boot.product.productChannel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductChannelDao
		extends JpaRepository<ProductChannel, String>, JpaSpecificationExecutor<ProductChannel> {
	
}
