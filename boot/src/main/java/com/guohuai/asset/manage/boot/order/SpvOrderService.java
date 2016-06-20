package com.guohuai.asset.manage.boot.order;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.ghg.pay.api.SeqGenerator;
import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolDao;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.investor.Investor;
import com.guohuai.asset.manage.boot.investor.InvestorAccount;
import com.guohuai.asset.manage.boot.investor.InvestorAccountDao;
import com.guohuai.asset.manage.boot.investor.InvestorDao;
import com.guohuai.asset.manage.boot.product.Product;
import com.guohuai.asset.manage.boot.product.ProductDao;
import com.guohuai.asset.manage.boot.product.ProductDecimalFormat;
import com.guohuai.asset.manage.boot.product.ProductDetailResp;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;

@Service
@Transactional
public class SpvOrderService {
	
	@Autowired
	private InvestorDao investorDao;
	@Autowired
	private InvestorAccountDao investorAccountDao;
	@Autowired
	private InvestorOrderDao investorOrderDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SeqGenerator seqGenerator;
	@Autowired
	private AssetPoolDao assetPoolDao;
	@Autowired
	private AdminSdk adminSdk;
	
	/**
	 * 获取指定oid的订单对象
	 * @param oid 订单对象id
	 * @return {@link InvestorOrder}
	 */
	public InvestorOrder getSpvOrderById(String oid) {
		InvestorOrder investorOrder = this.investorOrderDao.findOne(oid);
		if (investorOrder == null || InvestorOrder.STATUS_Disable.equals(investorOrder.getOrderStatus())) {
			throw AMPException.getException(100000);
		}
		return investorOrder;
		
	}
	
	public InvestorOrder saveSpvOrder(SaveSpvOrderForm form, String operator) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		AssetPoolEntity assetPool = assetPoolDao.findOne(form.getAssetPoolOid());
		if(assetPool == null) {
			throw AMPException.getException(30001);
		}
		
		List<Product> products = this.getProductListByAssetPoolOid(form.getAssetPoolOid());
		Product product = null;
		if(products!=null && products.size()>0) {
			product = products.get(0);
		}
		
		/***************持有人 start************/
		Investor investor = investorDao.findByType(Investor.TYPE_Spv);
		InvestorAccount oldInvestorAccount = null;
		
		if(investor != null) {
			final String investorOid = investor.getOid();
			Specification<InvestorAccount> iaSpec = new Specification<InvestorAccount>() {
				@Override
				public Predicate toPredicate(Root<InvestorAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(cb.equal(root.get("assetPool").get("oid").as(String.class), form.getAssetPoolOid()),
							cb.equal(root.get("investor").get("oid").as(String.class), investorOid),
							cb.equal(root.get("status").as(String.class), InvestorAccount.STATUS_Enable));
				}
			};
			oldInvestorAccount = investorAccountDao.findOne(iaSpec);
			
		}
		if(InvestorOrder.ORDER_TYPE_Redeem.equals(form.getOrderType())) {//判断能否赎回
			if(investor == null || oldInvestorAccount == null) {
				throw AMPException.getException(100001);
			}
//			if(investorAccount.getBalance().add(investorAccount.getCompound()).subtract(investorAccount.getApplyRedeem()).compareTo(new BigDecimal(form.getOrderAmount()))<0) {
//				throw AMPException.getException(100003);
//			}
		}
		String sn = seqGenerator.next("SPV");
		/***************持有人 end************/
		if(investor == null) {
			investor = new Investor();
			investor.setOid(StringUtil.uuid());
			investor.setType(Investor.TYPE_Spv);
			investor.setSn(sn);
			investor.setCreateTime(now);
			investor.setUpdateTime(now);
			investorDao.save(investor);
		} else {
			investor.setUpdateTime(now);
			investorDao.saveAndFlush(investor);
		}
		
		InvestorAccount investorAccount = null;
		if(oldInvestorAccount == null) {
			investorAccount = new InvestorAccount();
			investorAccount.setOid(StringUtil.uuid());
			investorAccount.setAssetPool(assetPool);
			investorAccount.setProduct(product);
			investorAccount.setInvestor(investor);
			investorAccount.setStatus(InvestorAccount.STATUS_Enable);
			if(InvestorOrder.ORDER_TYPE_Redeem.equals(form.getOrderType())) {
				investorAccount.setFreeze(ProductDecimalFormat.multiply(new BigDecimal(form.getOrderAmount()), 10000));
			}
			investorAccount.setCreateTime(now);
			investorAccount.setUpdateTime(now);
			investorAccountDao.save(investorAccount);
		} else {
			investorAccount = investorAccountDao.findOne(oldInvestorAccount.getOid());
			investorAccount.setProduct(product);
			investorAccount.setStatus(InvestorAccount.STATUS_Enable);
			if(InvestorOrder.ORDER_TYPE_Redeem.equals(form.getOrderType())) {
				investorAccount.setFreeze(investorAccount.getFreeze().add(ProductDecimalFormat.multiply(new BigDecimal(form.getOrderAmount()), 10000)));
			}
			investorAccount.setUpdateTime(now);
			investorAccountDao.saveAndFlush(investorAccount);
		}
		
		InvestorOrder investorOrder = new InvestorOrder();
		investorOrder.setOid(StringUtil.uuid());
		investorOrder.setAccount(investorAccount);//关联基本账户
		investorOrder.setOrderCode(sn);
		investorOrder.setOrderType(form.getOrderType());
		investorOrder.setOrderCate(form.getOrderCate());
		investorOrder.setOrderAmount(ProductDecimalFormat.multiply(new BigDecimal(form.getOrderAmount()), 10000));
		investorOrder.setOrderDate(DateUtil.parseToSqlDate(form.getOrderDate()));
		investorOrder.setOrderVolume(investorOrder.getOrderAmount());
		investorOrder.setOrderStatus(InvestorOrder.STATUS_Submit);
		investorOrder.setEntryStatus(InvestorOrder.ENTRY_STATUS_No);
		investorOrder.setOrderStem(InvestorOrder.ORDER_STEM_Plateform);
		investorOrder.setCreater(operator);
		investorOrder.setCreateTime(now);
		investorOrder.setUpdateTime(now);
		investorOrderDao.save(investorOrder);
		
		return investorOrder;
	}
	
	public InvestorOrder delete(String oid, String operator) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		InvestorOrder investorOrder = this.getSpvOrderById(oid);
		investorOrder.setOrderStatus(InvestorOrder.STATUS_Disable);
		investorOrder.setUpdateTime(now);
		investorOrderDao.saveAndFlush(investorOrder);
		
		InvestorAccount investorAccount = null;
		if(investorOrder.getAccount()!=null) {
			investorAccount = investorAccountDao.findOne(investorOrder.getAccount().getOid());
			investorAccount.setFreeze(investorAccount.getFreeze().subtract(investorOrder.getOrderAmount()));
			investorAccount.setUpdateTime(now);
			investorAccountDao.saveAndFlush(investorAccount);
		}
		
		return investorOrder;
	}
	
	public InvestorOrder confirm(AuditSpvOrderForm form, String operator) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		InvestorOrder investorOrder = this.getSpvOrderById(form.getOid());
		
		BigDecimal avaibleAmount = ProductDecimalFormat.multiply(new BigDecimal(form.getAvaibleAmount()), 10000);
		BigDecimal payFee = ProductDecimalFormat.multiply(new BigDecimal(form.getPayFee()), 10000);
		
		if(investorOrder.getOrderAmount().compareTo(avaibleAmount.add(payFee))!=0) {
			throw AMPException.getException(100004);
		}
		
		InvestorAccount investorAccount = null;
		if(investorOrder.getAccount()!=null) {
			investorAccount = investorAccountDao.findOne(investorOrder.getAccount().getOid());
			if(InvestorOrder.ORDER_TYPE_Redeem.equals(investorOrder.getOrderType())) {
				AssetPoolEntity assetPool = assetPoolDao.findOne(investorOrder.getAccount().getAssetPool().getOid());
				
				BigDecimal mv = assetPool.getMarketValue();
				BigDecimal av = investorAccount.getBalance().subtract(investorAccount.getFreeze());
				if(mv==null) {
					mv = new BigDecimal(0);
				}
				BigDecimal reemAmount = new BigDecimal(0);
				if(mv.compareTo(av)>0) {
					reemAmount = av;
				} else {
					reemAmount = mv;
				}
				if(avaibleAmount.compareTo(reemAmount)>0) {
					throw AMPException.getException(100003);
				}
				
				investorOrder.setOrderStatus(InvestorOrder.STATUS_Confirm);
				investorOrder.setAuditor(operator);
				investorOrder.setCompleteTime(now);
				investorOrder.setUpdateTime(now);
				investorOrder.setPayFee(investorOrder.getPayFee().add(payFee));
				
				investorAccount.setFreeze(investorAccount.getFreeze().subtract(investorOrder.getOrderAmount()));
				investorAccount.setBalance(investorAccount.getBalance().subtract(avaibleAmount));
				investorAccount.setUpdateTime(now);
				
				
				assetPool.setMarketValue(assetPool.getMarketValue().subtract(avaibleAmount));
				assetPool.setUpdateTime(now);
				
				Product product = null;
				if(investorOrder.getAccount().getProduct()!=null) {
					product = productDao.findOne(investorOrder.getAccount().getProduct().getOid());
					product.setRaisedTotalNumber(product.getRaisedTotalNumber().subtract(avaibleAmount));
					product.setUpdateTime(now);
				}
				
				investorOrderDao.saveAndFlush(investorOrder);
				investorAccountDao.saveAndFlush(investorAccount);
				
				if(product!=null) {
					productDao.saveAndFlush(product);
				}
				assetPoolDao.saveAndFlush(assetPool);
			} else if(InvestorOrder.ORDER_TYPE_Invest.equals(investorOrder.getOrderType())) {
				investorOrder.setOrderStatus(InvestorOrder.STATUS_Confirm);
				investorOrder.setAuditor(operator);
				investorOrder.setCompleteTime(now);
				investorOrder.setUpdateTime(now);
				investorOrder.setPayFee(investorOrder.getPayFee().add(payFee));
				
				investorAccount.setBalance(investorAccount.getBalance().add(avaibleAmount));
				investorAccount.setUpdateTime(now);
				
				AssetPoolEntity assetPool = assetPoolDao.findOne(investorOrder.getAccount().getAssetPool().getOid());
				if(assetPool.getMarketValue()==null) {
					assetPool.setMarketValue(new BigDecimal(0));
				}
				assetPool.setMarketValue(assetPool.getMarketValue().add(avaibleAmount));
				assetPool.setUpdateTime(now);
				
				Product product = null;
				if(investorOrder.getAccount().getProduct()!=null) {
					product = productDao.findOne(investorOrder.getAccount().getProduct().getOid());
					if(product.getRaisedTotalNumber()==null) {
						product.setRaisedTotalNumber(new BigDecimal(0));
					}
					product.setRaisedTotalNumber(product.getRaisedTotalNumber().add(avaibleAmount));
					product.setUpdateTime(now);
				}
				
				investorOrderDao.saveAndFlush(investorOrder);
				investorAccountDao.saveAndFlush(investorAccount);
				
				if(product!=null) {
					productDao.saveAndFlush(product);
				}
				assetPoolDao.saveAndFlush(assetPool);
			}
			
		}
					
		return investorOrder;
	}
	
	
	public BigDecimal reemAmount(String assetPoolOid) {
//		Specification<InvestorAccount> iaSpec = new Specification<InvestorAccount>() {
//			@Override
//			public Predicate toPredicate(Root<InvestorAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				return cb.and(cb.equal(root.get("assetPool").get("oid").as(String.class), assetPoolOid),
//						cb.equal(root.get("status").as(String.class), InvestorAccount.STATUS_Enable));
//			}
//		};
//		InvestorAccount investorAccount = investorAccountDao.findOne(iaSpec);
//		if(investorAccount!=null) {
//			return investorAccount.getBalance().add(investorAccount.getCompound()).subtract(investorAccount.getApplyRedeem());
//		}
					
		return new BigDecimal(0);
	}
	
	public ProductDetailResp getProduct(String assetPoolOid) {
		
		List<Product> products = getProductListByAssetPoolOid(assetPoolOid);
		ProductDetailResp pr = null;
		if(products!=null && products.size()>0) {
			pr = new ProductDetailResp(products.get(0));
		} else {
			pr = new ProductDetailResp();
		}
		return pr;
	}
	
	public List<Product> getProductListByAssetPoolOid(String assetPoolOid) {
		
		Specification<Product> spec = new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("assetPool").get("oid").as(String.class), assetPoolOid),
						cb.equal(root.get("isDeleted").as(String.class), Product.NO));
			}
		};
		List<Product> products = productDao.findAll(spec);
					
		return products;
	}
	
	public PageResp<SpvOrderResp> list(Specification<InvestorOrder> spec, Pageable pageable) {
		PageResp<SpvOrderResp> pagesRep = new PageResp<SpvOrderResp>();
		
		Page<InvestorOrder> cas = this.investorOrderDao.findAll(spec, pageable);
		
		if (cas != null && cas.getContent() != null && cas.getTotalElements() > 0) {
			List<SpvOrderResp> rows = new ArrayList<SpvOrderResp>();
			
			Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
			
			AdminObj adminObj = null;
			for (InvestorOrder order : cas) {
				SpvOrderResp spvOrderRep = new SpvOrderResp(order);
				
				if(adminObjMap.get(order.getCreater())==null) {
					try{
						adminObj = adminSdk.getAdmin(order.getCreater());
						adminObjMap.put(order.getCreater(),adminObj);
					} catch (Exception e) {
					}
				}
				if(adminObjMap.get(order.getCreater())!=null) {
					spvOrderRep.setCreater(adminObjMap.get(order.getCreater()).getName());
				}
					
				if(adminObjMap.get(order.getAuditor())==null) {
					try{
						adminObj = adminSdk.getAdmin(order.getAuditor());
						adminObjMap.put(order.getAuditor(),adminObj);
					} catch (Exception e) {
					}
				}
				if(adminObjMap.get(order.getAuditor())!=null) {
					spvOrderRep.setAuditor(adminObjMap.get(order.getAuditor()).getName());
				}
				rows.add(spvOrderRep);
			}
			pagesRep.setRows(rows);
		}
		pagesRep.setTotal(cas.getTotalElements());
		
		return pagesRep;
	}
	
	public SpvOrderDetailResp detail(String oid) {
		
		InvestorOrder order = this.investorOrderDao.findOne(oid);
		
		SpvOrderDetailResp spvOrderRep = new SpvOrderDetailResp(order);
		
		Map<String,AdminObj> adminObjMap = new HashMap<String,AdminObj>();
		
		AdminObj adminObj = null;
		
		if(adminObjMap.get(order.getCreater())==null) {
			try{
				adminObj = adminSdk.getAdmin(order.getCreater());
				adminObjMap.put(order.getCreater(),adminObj);
			} catch (Exception e) {
			}
		}
		if(adminObjMap.get(order.getCreater())!=null) {
			spvOrderRep.setCreater(adminObjMap.get(order.getCreater()).getName());
		}
		
		if(adminObjMap.get(order.getAuditor())==null) {
			try{
				adminObj = adminSdk.getAdmin(order.getAuditor());
				adminObjMap.put(order.getAuditor(),adminObj);
			} catch (Exception e) {
			}
		}
		if(adminObjMap.get(order.getAuditor())!=null) {
			spvOrderRep.setAuditor(adminObjMap.get(order.getAuditor()).getName());
		}
		return spvOrderRep;
	}
	
	/**
	 * 查询满足条件的订单列表，计算资产池的实际市值
	 * @param pid
	 * 			资产池id
	 * @param baseDate
	 * 			基准日
	 * @author star
	 * @return
	 */
	public List<Object[]> getListForMarketAdjust(String pid, Date baseDate) {
		List<InvestorOrder> list = investorOrderDao.getListForMarketAdjust(pid, baseDate);
		if (null != list && !list.isEmpty()) {
			List<Object[]> objList = Lists.newArrayList();
			Object[] obj = null;
			for (InvestorOrder order : list) {
				obj = new Object[3];
				obj[0] = order.getOid();
				obj[1] = order.getOrderType();
				obj[2] = order.getOrderAmount();
				objList.add(obj);
			}
			return objList;
		}
		
		return null;
	}

}
