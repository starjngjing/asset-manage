package com.guohuai.asset.manage.boot.duration.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;

public interface OrderDao extends JpaRepository<AssetPoolEntity, String>, JpaSpecificationExecutor<AssetPoolEntity> {

}
