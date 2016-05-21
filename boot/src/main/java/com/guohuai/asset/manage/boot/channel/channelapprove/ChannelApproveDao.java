package com.guohuai.asset.manage.boot.channel.channelapprove;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.guohuai.asset.manage.boot.channel.Channel;

public interface ChannelApproveDao extends JpaRepository<ChannelApprove, String>, JpaSpecificationExecutor<ChannelApprove> {

	@Query(value = "FROM ChannelApprove c WHERE c.channel = ?1 ORDER BY c.updateTime DESC")
	public List<ChannelApprove> getByChannel(Channel channel);
	
	public List<ChannelApprove> findByChannelAndApprovelResult(Channel en, String approveResult);
}
