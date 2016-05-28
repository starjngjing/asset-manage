package com.guohuai.asset.manage.boot.channel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.channel.ChannelQueryRep.ChannelQueryRepBuilder;
import com.guohuai.asset.manage.boot.channel.ChannelRemarksRep.ChannelRemarksRepBuilder;
import com.guohuai.asset.manage.boot.channel.channelapprove.ChannelApprove;
import com.guohuai.asset.manage.boot.channel.channelapprove.ChannelApproveService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.Clock;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;

@Service
@Transactional
public class ChannelService {
	
	@Autowired
	ChannelDao daoChannel;
	@Autowired
	ChannelApproveService serviceChannelApprove;	
	
	@Transactional
	public PageResp<ChannelQueryRep> channelQuery(Specification<Channel> spec, Pageable pageable) {		
		Page<Channel> enchs = this.daoChannel.findAll(spec, pageable);
		PageResp<ChannelQueryRep> pageResp = new PageResp<ChannelQueryRep>();
		List<ChannelQueryRep> list = new ArrayList<ChannelQueryRep>();
		for (Channel ench : enchs) {
			ChannelQueryRep rep = new ChannelQueryRepBuilder().oid(ench.getOid())
					.channelCode(ench.getChannelCode()).channelName(ench.getChannelName())
					.channelId(ench.getChannelId()).channelFee(ench.getChannelFee())
					.joinType(ench.getJoinType()).partner(ench.getPartner())
					.channelContactName(ench.getChannelContactName()).channelStatus(ench.getChannelStatus())
					.approvelStatus(ench.getApproveStatus()).deleteStatus(ench.getDeleteStatus())
					.build();
			list.add(rep);
		}
		pageResp.setTotal(enchs.getTotalElements());
		pageResp.setRows(list);
		return pageResp;
	}
	
	/**
	 * 获取渠道实体
	 * @param oid
	 * @return
	 */
	public Channel getOne(String oid){
		Channel en = this.daoChannel.findOne(oid);
		if(en==null){
			//error.define[70001]=平台-渠道不存在!(CODE:70001)
			throw AMPException.getException(70001);
		}
		return en;
	}
	
	/**
	 * 保存渠道
	 * @param en
	 * @return
	 */
	public Channel save(Channel en){
		return this.daoChannel.save(en);
	}
	
	/**
	 * 获取渠道详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public ChannelInfoRep getChannelInfo(String oid){		
		Channel en = this.getOne(oid);
		ChannelInfoRep rep = new ChannelInfoRep();
		rep.setOid(en.getOid());		
		rep.setChannelName(en.getChannelName());	
		rep.setChannelId(en.getChannelId());
		rep.setChannelFee(en.getChannelFee());
		rep.setJoinType(en.getJoinType());
		rep.setPartner(en.getPartner());
		rep.setChannelContactName(en.getChannelContactName());
		rep.setChannelEmail(en.getChannelEmail());
		rep.setChannelPhone(en.getChannelPhone());			
		rep.setChannelStatus(en.getChannelStatus());    
		rep.setApprovelStatus(en.getApproveStatus());
		rep.setDeleteStatus(en.getDeleteStatus());
		return rep;
	}
	
	/**
	 * 渠道的开启停用申请
	 * @param oid 渠道Oid
	 * @param requestType 申请类型
	 * @param operator
	 * @return
	 */
	@Transactional
	public BaseResp setApply(String oid, String requestType, String operator){
		BaseResp rep = new BaseResp();
		Timestamp now = new Timestamp(Clock.DEFAULT.getCurrentTimeInMillis());
		Channel chan = this.getOne(oid);
		//查看审批表是否有未审核的记录
		List<ChannelApprove> lists = serviceChannelApprove.findByChannelAndApprovelResult(chan);
		if(lists!=null && lists.size()>0){
			//error.define[70004]=已经存在一条未审批的记录，无法再次操作!(CODE:70004)
			throw AMPException.getException(70004);
		}
		ChannelApprove channelApprove = new ChannelApprove();
		channelApprove.setChannel(chan);
		channelApprove.setChannelName(chan.getChannelName());
		channelApprove.setChannelApproveCode(StringUtil.uuid());
		channelApprove.setRequestType(requestType);
		//申请人
		channelApprove.setRequester(operator);
		channelApprove.setRequestTime(now);
		//审批表 标记为待审批
		channelApprove.setApproveResult(ChannelApprove.CHANAPPROVE_APPROVESTATUS_toApprove);
		channelApprove.setCreateTime(now);
		channelApprove.setUpdateTime(now);
		this.serviceChannelApprove.saveChanApprEntity(channelApprove);
		//待审批
		chan.setApproveStatus(Channel.CHANNEL_APPROVESTATUS_toApprove);
		chan.setUpdateTime(now);
		this.daoChannel.save(chan);
		return rep;
	}
	
	/**
	 * 新增和修改渠道
	 * @param req
	 * @return
	 */
	public Channel addChannel(ChannelAddReq req){
		Timestamp now = new Timestamp(Clock.DEFAULT.getCurrentTimeInMillis());
		Channel channel = null;
		if(req.getOid()!=null && !"".equals(req.getOid())){
			channel = this.getOne(req.getOid());
		}else{
			channel = new Channel();
			channel.setCreateTime(now);
		}
		//此处channelId以后修改成想要的格式
		channel.setChannelId(StringUtil.uuid());
		channel.setChannelName(req.getChannelName());
		channel.setJoinType(req.getJoinType());
		channel.setChannelFee(req.getChannelFee());
		channel.setPartner(req.getPartner());
		channel.setChannelContactName(req.getChannelContactName());
		channel.setChannelEmail(req.getChannelEmail());
		channel.setChannelPhone(req.getChannelPhone());		
		channel.setChannelStatus(Channel.CHANNEL_STATUS_OFF);
		//删除状态-正常
		channel.setDeleteStatus(Channel.CHANNEL_DELESTATUS_NO);
		channel.setUpdateTime(now);
		channel = this.daoChannel.save(channel);
		return channel;
	}
	
	/**
	 * 删除渠道
	 * @param oid
	 * @return
	 */
	public BaseResp delChannel(final String oid){
		BaseResp rep = new BaseResp();
		Timestamp now = new Timestamp(Clock.DEFAULT.getCurrentTimeInMillis());
		//判断渠道是否存在
		Channel en = this.getOne(oid);
		en.setDeleteStatus(Channel.CHANNEL_DELESTATUS_YES);
		en.setUpdateTime(now);
		this.daoChannel.save(en);		
		return rep;
	}
	
	/**
	 * 根据渠道获取审批表中的审核意见列表
	 * @param oid
	 * @return
	 */
	@Transactional
	public PageResp<ChannelRemarksRep> remarksQuery(String oid) {
		Channel channel = this.getOne(oid);
		List<ChannelApprove> list = this.serviceChannelApprove.findByChannel(channel);
		
		PageResp<ChannelRemarksRep> pageResp = new PageResp<ChannelRemarksRep>();
		List<ChannelRemarksRep> listReps = new ArrayList<ChannelRemarksRep>();
		for (ChannelApprove en : list) {
			ChannelRemarksRep rep = new ChannelRemarksRepBuilder().remark(en.getRemark())
					.time(en.getUpdateTime()).build();
			listReps.add(rep);
		}
		pageResp.setRows(listReps);
		return pageResp;
	}
	
	/**
	 * 获取一个渠道信息
	 * @return
	 */
	public ChannelInfoRep getOneChannel(){
		List<Channel> list = this.daoChannel.findAll();
		ChannelInfoRep rep = new ChannelInfoRep();
		if(list!=null && list.size()>0){			
			rep = this.getChannelInfo(list.get(0).getOid());
		}
		return rep;
	}
}
