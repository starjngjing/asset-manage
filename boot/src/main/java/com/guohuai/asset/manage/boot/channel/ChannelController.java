package com.guohuai.asset.manage.boot.channel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;

import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
@RestController
@RequestMapping(value = "/ams/channel", produces = "application/json")
public class ChannelController extends BaseController {

	@Autowired
	private ChannelService serviceChannel;

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<PageResp<ChannelQueryRep>> channelQuery(HttpServletRequest request,
			@And({  @Spec(params = "channelName", path = "channelName", spec = Like.class),
					@Spec(params = "channelCode", path = "channelCode", spec = Like.class),
					@Spec(params = "partner", path = "partner", spec = Like.class),
					@Spec(params = "contactName", path = "channelContactName", spec = Like.class),
					@Spec(params = "joinType", path = "joinType", spec = In.class),
					@Spec(params = "channelStatus", path = "channelStatus", spec = In.class),
					@Spec(params = "delStatus", path = "deleteStatus", spec = In.class)}) 
	         		Specification<Channel> spec,
			@RequestParam int page, 
			@RequestParam int rows) {		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(Direction.DESC, "createTime")));		
		PageResp<ChannelQueryRep> rep = this.serviceChannel.channelQuery(spec, pageable);
		
		return new ResponseEntity<PageResp<ChannelQueryRep>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 获取渠道详情
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "channelinfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ChannelInfoRep> getInfo(@RequestParam String oid) {		
		ChannelInfoRep rep = this.serviceChannel.getChannelInfo(oid);
		return new ResponseEntity<ChannelInfoRep>(rep, HttpStatus.OK);
	}
	
	/**
	 * 新增渠道
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> addChanInfo(@Valid ChannelAddReq req) {		
		BaseResp rep = new BaseResp();
		this.serviceChannel.addChannel(req);
		return new ResponseEntity<BaseResp>(rep, HttpStatus.OK);		
	}
	
	/**
	 * 修改渠道
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> editChanInfo(@Valid ChannelAddReq req) {		
		BaseResp rep = new BaseResp();
		this.serviceChannel.addChannel(req);
		return new ResponseEntity<BaseResp>(rep, HttpStatus.OK);		
	}
	
	/**
	 * 删除渠道
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> delChanInfo(@RequestParam String oid) {		
		BaseResp rep = this.serviceChannel.delChannel(oid);
		return new ResponseEntity<BaseResp>(rep, HttpStatus.OK);		
	}
	
	/**
	 * 渠道申请处理
	 * @param oid
	 * @param requestType 申请类型 on/off
	 * @return
	 */
	@RequestMapping(value = "setapply", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<BaseResp> setApply(@RequestParam String oid, @RequestParam String requestType) {
		//String operator = super.getLoginAdmin();
		BaseResp rep = this.serviceChannel.setApply(oid, requestType, "dev");
		return new ResponseEntity<BaseResp>(rep, HttpStatus.OK);
	}
	
	/**
	 * 获取审批表中审核意见列表
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "remarksquery", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<PageResp<ChannelRemarksRep>> remarksQuery(@RequestParam String oid) {		
		
		PageResp<ChannelRemarksRep> rep = this.serviceChannel.remarksQuery(oid);
		
		return new ResponseEntity<PageResp<ChannelRemarksRep>>(rep, HttpStatus.OK);
	}
	
}
