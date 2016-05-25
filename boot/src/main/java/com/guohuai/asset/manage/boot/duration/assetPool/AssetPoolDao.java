package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AssetPoolDao extends JpaRepository<AssetPoolEntity, String>, JpaSpecificationExecutor<AssetPoolEntity> {

	// 获取所有的资产池id和名称列表
	@Query(value = "SELECT a.oid, a.name FROM T_GAM_ASSETPOOL a", nativeQuery = true)
	public List<Object> findAllNameList();
	
	@Query("from AssetPoolEntity a where a.name like ?1")
	public List<AssetPoolEntity> getListByName(String name);
}
