package com.guohuai.asset.manage.boot.product.productChannel;

import java.text.ParseException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.guohuai.asset.manage.boot.channel.Channel;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Api("产品关联渠道操作相关接口")
@RestController
@RequestMapping(value = "/ams/product/channel", produces = "application/json")
public class ProductChannelController extends BaseController {

	@Autowired
	private ProductChannelService productChannelService;
	
	/**
	 * 产品选择渠道列表
	 * @param request
	 * @param productOid
	 * @return
	 */
	@RequestMapping(value = "/choose/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<PageResp<ChooseChannelResp>> chooseList(HttpServletRequest request,@RequestParam(required = true) String productOid) {
		
		Direction sortDirection = Direction.DESC;
		Specification<Channel> spec = new Specification<Channel>() {
			@Override
			public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("deleteStatus").as(String.class), Channel.CHANNEL_DELESTATUS_NO), cb.equal(root.get("approvelStatus").as(String.class), Channel.CHANNEL_APPROVESTATUS_PASS));
			}
		};
		spec = Specifications.where(spec);
		
		PageResp<ChooseChannelResp> rep = this.productChannelService.chooseChannelQuery(productOid, spec, new Sort(new Order(sortDirection, "updateTime")));
		return new ResponseEntity<PageResp<ChooseChannelResp>>(rep, HttpStatus.OK);
	}
	
	
	/**
	 * 保存产品已经选择渠道列表
	 * @param request
	 * @param productOid
	 * @param channelOids
	 * @return
	 */
	@RequestMapping(value = "/save/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<BaseResp> saveApplyChannel(HttpServletRequest request, 
			@RequestParam(required = true) String productOid, 
			@RequestParam(required = true) String channelOid) throws ParseException {
		String operator = super.getLoginAdmin();
		List<String> channelOids = JSON.parseArray(channelOid, String.class);
		BaseResp repponse = this.productChannelService.saveApplyChannel(operator, productOid, channelOids);
		return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}
	
	
	/**
	 * 该渠道的产品列表
	 * @param request
	 * @param productOid
	 * @param channelOids
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<PageResp<ProductChannelResp>> list(HttpServletRequest request, @RequestParam(required = true) String channelOid, 
			@RequestParam int page, @RequestParam int rows) throws ParseException {
		if (page < 1) {
			page = 1;
		}
		if (rows < 1) {
			rows = 1;
		}
		
		Direction sortDirection = Direction.DESC;
		Specification<ProductChannel> spec = new Specification<ProductChannel>() {
			@Override
			public Predicate toPredicate(Root<ProductChannel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("channel").get("oid").as(String.class), channelOid);
			}
		};
		spec = Specifications.where(spec);
		
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, "createTime")));
		PageResp<ProductChannelResp> rep = this.productChannelService.list(spec, pageable);
		return new ResponseEntity<PageResp<ProductChannelResp>>(rep, HttpStatus.OK);
	}
	
	/**
	 * 上架
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/upshelf", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<BaseResp> upshelf(@RequestParam(required = true) String oid) throws ParseException {
		String operator = super.getLoginAdmin();
		BaseResp r = this.productChannelService.upshelf(oid, operator);
		return new ResponseEntity<BaseResp>(r, HttpStatus.OK);
	}
	
	/**
	 * 下架
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/donwshelf",method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<BaseResp> donwshelf(@RequestParam String oid) {
		String operator = super.getLoginAdmin();
		BaseResp r = this.productChannelService.donwshelf(oid, operator);
		return new ResponseEntity<BaseResp>(r, HttpStatus.OK);
	}
	
}
