package com.guohuai.asset.manage.boot.Duration.assetPool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AssetPoolDao extends JpaRepository<AssetPoolEntity, String>, JpaSpecificationExecutor<AssetPoolEntity> {

}
