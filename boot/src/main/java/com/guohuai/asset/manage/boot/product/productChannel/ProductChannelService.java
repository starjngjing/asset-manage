package com.guohuai.asset.manage.boot.product.productChannel;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.guohuai.asset.manage.boot.channel.Channel;
import com.guohuai.asset.manage.boot.channel.ChannelDao;
import com.guohuai.asset.manage.boot.product.Product;
import com.guohuai.asset.manage.boot.product.ProductDao;
import com.guohuai.asset.manage.boot.product.ProductService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@Transactional
public class ProductChannelService {
	
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ProductChannelDao productChannelDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductService productService;
	
	@Transactional
	public PageResp<ChooseChannelResp> chooseChannelQuery(String productOid, Specification<Channel> spec, Sort sort) {		
		PageResp<ChooseChannelResp> pagesRep = new PageResp<ChooseChannelResp>();
		
		List<ProductChannel> pcs= queryProductChannels(productOid);
		List<String> channelOids= new ArrayList<String>();
		if(pcs!=null && pcs.size()>0) {
			for(ProductChannel pc : pcs) {
				channelOids.add(pc.getChannel().getOid());
			}
		}
		
		List<Channel> channels = this.channelDao.findAll(spec, sort);
		List<ChooseChannelResp> list = new ArrayList<ChooseChannelResp>();
		for (Channel channel : channels) {
			ChooseChannelResp rep = new ChooseChannelResp(channel);
			if(channelOids.contains(channel.getOid())) {
				rep.setSelectStatus(true);
			} else {
				rep.setSelectStatus(false);
			}
			list.add(rep);
		}
		pagesRep.setTotal(channels.size());
		pagesRep.setRows(list);
		return pagesRep;
	}
	
	@Transactional
	public List<ProductChannel> queryProductChannels(String productOid) {		
		
		Specification<ProductChannel> spec = new Specification<ProductChannel>() {
			@Override
			public Predicate toPredicate(Root<ProductChannel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("product").get("oid").as(String.class), productOid);
			}
		};
		spec = Specifications.where(spec);
		
		List<ProductChannel> pcs = productChannelDao.findAll(spec);
		
		return pcs;
	}
	
	@Transactional
	public List<ProductChannel> queryProductChannels(List<String> productOids) {		
		
		Specification<ProductChannel> spec = new Specification<ProductChannel>() {
			@Override
			public Predicate toPredicate(Root<ProductChannel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<String> exp = root.get("product").get("oid").as(String.class);
				return exp.in(productOids);
			}
		};
		spec = Specifications.where(spec);
		
		List<ProductChannel> pcs = productChannelDao.findAll(spec);
		
		return pcs;
	}
	
	@Transactional
	public BaseResp saveApplyChannel(String operator, String productOid, List<String> channelOids) throws ParseException {

		BaseResp response = new BaseResp();
		
		Product product = productService.getProductById(productOid);
		
		List<Channel> channels = this.channelDao.findByOidIn(channelOids);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		List<ProductChannel> delProductChannels =  new ArrayList<ProductChannel>();
		List<String> exiChannelOids = new ArrayList<String>();
		
		List<ProductChannel> productChannels= queryProductChannels(productOid);
		if(productChannels!=null && productChannels.size()>0) {
			for(ProductChannel pc : productChannels) {
				if(channelOids.contains(pc.getChannel().getOid())) {
					exiChannelOids.add(pc.getChannel().getOid());
				} else {
					delProductChannels.add(pc);
				}
			}
		}
		List<ProductChannel> newProductChannels =  new ArrayList<ProductChannel>();
		for(Channel channel : channels) {
			if(Channel.CHANNEL_APPROVESTATUS_PASS.equals(channel.getApproveStatus()) && Channel.CHANNEL_DELESTATUS_NO.equals(channel.getDeleteStatus())) {
				if(!exiChannelOids.contains(channel.getOid())) {
					ProductChannel.ProductChannelBuilder productChannelBuilder = ProductChannel.builder().oid(StringUtil.uuid());
					{
						productChannelBuilder.product(product).channel(channel).marketState(ProductChannel.MARKET_STATE_Noshelf);
					}
					{
						productChannelBuilder.operator(operator).createTime(now).updateTime(now);
					}
					
					newProductChannels.add(productChannelBuilder.build());
				}
			} else {
				throw AMPException.getException(90016);
			}
		}
		if(delProductChannels.size()>0) {
			for(ProductChannel pc : delProductChannels) {
				this.productChannelDao.delete(pc);
			}
		}
		if(newProductChannels.size()>0) {
			for(ProductChannel productChannel:newProductChannels) {
				this.productChannelDao.save(productChannel);
			}
			
		}
		return response;
	}
	
	@Transactional
	public PageResp<ProductChannelResp> list(Specification<ProductChannel> spec, Pageable pageable) {		
		PageResp<ProductChannelResp> pagesRep = new PageResp<ProductChannelResp>();
		
		Page<ProductChannel> productChannels = productChannelDao.findAll(spec, pageable);
		
		List<ProductChannelResp> list = new ArrayList<ProductChannelResp>();
		for (ProductChannel pc : productChannels) {
			ProductChannelResp rep = new ProductChannelResp(pc);
			list.add(rep);
		}
		pagesRep.setTotal(productChannels.getTotalElements());
		pagesRep.setRows(list);
		return pagesRep;
	}
	
		
	@Transactional
	public BaseResp upshelf(String oid, String operator) {
		BaseResp response = new BaseResp();
		ProductChannel productChannel = this.productChannelDao.findOne(oid);
		if (productChannel == null) {
			throw AMPException.getException(90017);//不能上架
		}
		Product product = this.productService.getProductById(productChannel.getProduct().getOid());
		Channel channel = this.channelDao.findOne(productChannel.getChannel().getOid());

		if(!Product.STATE_Admitpass.equals(product.getState())){
			throw AMPException.getException(90017);
		}
		if(Channel.CHANNEL_APPROVESTATUS_PASS.equals(channel.getApproveStatus()) && Channel.CHANNEL_DELESTATUS_NO.equals(channel.getDeleteStatus())) {
			if(!ProductChannel.MARKET_STATE_Noshelf.equals(productChannel.getMarketState())
					|| !ProductChannel.MARKET_STATE_Offshelf.equals(productChannel.getMarketState())) {
				throw AMPException.getException(90017);
			}
			productChannel.setMarketState(ProductChannel.MARKET_STATE_Onshelf);
			productChannel.setRackTime(new Timestamp(System.currentTimeMillis()));
			
			this.productChannelDao.saveAndFlush(productChannel);
			
			if(Product.DATE_TYPE_FirstRackTime.equals(product.getRaiseStartDateType()) && product.getRaiseStartDate()==null) {
				java.util.Date raiseStatrtDate = new java.util.Date();
				product.setRaiseStartDate(new Date(raiseStatrtDate.getTime()));
				product.setRaiseEndDate(new Date(DateUtil.addDay(raiseStatrtDate, product.getRaisePeriodDays()).getTime()));//募集结束时间
				product.setSetupDate(new Date(DateUtil.addDay(raiseStatrtDate, product.getRaisePeriodDays()+product.getInterestsFirstDays()).getTime()));//产品成立时间（存续期开始时间）
				product.setDurationPeriodEndDate(new Date(DateUtil.addDay(raiseStatrtDate, product.getRaisePeriodDays()+product.getInterestsFirstDays()+product.getDurationPeriodDays()).getTime()));//存续期结束时间
				//到期最晚还本付息日 指存续期结束后的还本付息最迟发生在存续期后的第X个自然日的23:59:59为止
				java.util.Date repayDate = DateUtil.addDay(raiseStatrtDate, product.getRaisePeriodDays()+product.getInterestsFirstDays()+product.getDurationPeriodDays()+product.getAccrualRepayDays());
				product.setRepayDate(new Date(repayDate.getTime()));//到期还款时间
				productDao.saveAndFlush(product);
			} else if(Product.DATE_TYPE_FirstRackTime.equals(product.getSetupDateType()) && product.getSetupDate()==null) {
				java.util.Date setupDate = new java.util.Date();
				product.setSetupDate(new Date(setupDate.getTime()));
				productDao.saveAndFlush(product);
			}
		} else {
			throw AMPException.getException(90017);
		}
		return response;
	}
	
	@Transactional
	public BaseResp donwshelf(String oid, String operator) {
		BaseResp response = new BaseResp();
		
		ProductChannel productChannel = this.productChannelDao.findOne(oid);
		if (productChannel == null) {
			throw AMPException.getException(90017);//不能下架
		}

		if(!ProductChannel.MARKET_STATE_Onshelf.equals(productChannel.getMarketState())) {
			throw AMPException.getException(90017);
		}
		productChannel.setMarketState(ProductChannel.MARKET_STATE_Offshelf);
		productChannel.setDownTime(new Timestamp(System.currentTimeMillis()));
		
		this.productChannelDao.saveAndFlush(productChannel);
		
		return response;
	}
	
}
