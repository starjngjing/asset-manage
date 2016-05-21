package com.guohuai.asset.manage.boot.channel;

import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
@lombok.Builder
public class ChannelQueryRep {
	
    private String oid, channelCode, channelName, channelId, channelFee, joinType, partner, 
                   channelContactName, channelStatus, approvelStatus, deleteStatus;
	
}
