package com.guohuai.asset.manage.boot.duration.assetPool;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AssetPoolDao extends JpaRepository<AssetPoolEntity, String>, JpaSpecificationExecutor<AssetPoolEntity> {

	// 获取所有已成立的资产池id和名称列表
	@Query(value = "SELECT a.oid, a.name FROM T_GAM_ASSETPOOL a WHERE a.state = 'ASSETPOOLSTATE_02' order by a.createTime desc", nativeQuery = true)
	public List<Object> findAllNameList();
	
	@Query("from AssetPoolEntity a where a.name like ?1")
	public List<AssetPoolEntity> getListByName(String name);
	
	@Query(value = "SELECT * FROM T_GAM_ASSETPOOL a WHERE a.state = 'ASSETPOOLSTATE_02' order by a.createTime desc LIMIT 1", nativeQuery = true)
	public AssetPoolEntity getLimitOne();
	
	@Query("from AssetPoolEntity a where a.state = 'ASSETPOOLSTATE_02'")
	public List<AssetPoolEntity> getListByState();
	
	@Query(value = "update T_GAM_ASSETPOOL set state = '已失效' where oid = ?1", nativeQuery = true)
	@Modifying
	public void updateAssetPool(String pid);
}
