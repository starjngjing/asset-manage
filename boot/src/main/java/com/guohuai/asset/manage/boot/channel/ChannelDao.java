package com.guohuai.asset.manage.boot.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChannelDao extends JpaRepository<Channel, String>, JpaSpecificationExecutor<Channel> {

	
}
