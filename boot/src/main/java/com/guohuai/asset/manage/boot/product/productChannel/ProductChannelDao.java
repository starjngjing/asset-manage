package com.guohuai.asset.manage.boot.product.productChannel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductChannelDao
		extends JpaRepository<ProductChannel, String>, JpaSpecificationExecutor<ProductChannel> {

//	
//	/**
//	 * @param productOid
//	 */
//	@Query("select * from T_REEF_CHANNEL_PRODUCT s where s.productOid = ?1 and s.channelOid = ?2")
//	public ProductChannel findProductChannel(String productOid, String channelOid);
//	
//	/**
//	 * 删除该渠道下所有商品和渠道的关联关系
//	 * @param channelOid
//	 */
//	@Query("delete from T_REEF_CHANNEL_PRODUCT s where s.productOid = ?1")
//	@Modifying
//	public void deleteByProductOid(String productOid);
//	
//	/**
//	 * @param productOid
//	 */
//	@Query("select * from T_REEF_CHANNEL_PRODUCT s where s.channelOid = ?1 and s.productOid in ?2")
//	public List<ProductChannel> findProductChannels(String channelOid,List<String> productOids);
	
}
