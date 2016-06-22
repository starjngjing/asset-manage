package com.guohuai.asset.manage.boot.duration.fact.income;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import com.guohuai.asset.manage.boot.acct.books.AccountBook;
import com.guohuai.asset.manage.boot.acct.books.AccountBookService;
import com.guohuai.asset.manage.boot.acct.books.document.SPVDocumentService;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.product.Product;
import com.guohuai.asset.manage.boot.product.ProductDecimalFormat;
import com.guohuai.asset.manage.boot.product.ProductService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import com.guohuai.asset.manage.component.web.view.PageResp;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;

/**
 * 收益分配
 * @author wangyan
 *
 */
@Service
public class IncomeDistributionService {
	
	@Autowired
	private IncomeEventDao incomeEventDao;
	@Autowired
	private IncomeAllocateDao incomeAllocateDao;
	@Autowired
	private AccountBookService accountBookService;
	@Autowired
	private SPVDocumentService spvDocumentService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AssetPoolService poolService;
	@Autowired
	private AdminSdk adminSdk;
	
	public IncomeAllocateCalcResp getIncomeAdjustData(String assetpoolOid) {
		IncomeAllocateCalcResp resp = new IncomeAllocateCalcResp();
		IncomeEvent ie = this.findTodayIncomeEvent(assetpoolOid);
		if(ie!=null) {
			resp.setErrorCode(-1);
			resp.setErrorMessage("今日已经申请过收益分配");
			throw AMPException.getException(60001);
		} else {
			
			BigDecimal undisIncome = new BigDecimal(0);//未分配收益
			BigDecimal productTotalScale = new BigDecimal(200000);//产品总规模 王国处获取
			BigDecimal productRewardBenefit = new BigDecimal(100000);//奖励收益 王国处获取
			BigDecimal productDistributionIncome = new BigDecimal(0);//分配收益
			BigDecimal productAnnualYield = new BigDecimal(0);//年化收益率
			
			Map<String, AccountBook>  accountBookMap = accountBookService.find(assetpoolOid, "1111","1201","2201");
			if(accountBookMap!=null && accountBookMap.size()>0) {
				//资产池
				AccountBook investmentAssets = accountBookMap.get("1111");//资产池 投资资产
				if(investmentAssets!=null) {
					resp.setInvestmentAssets(ProductDecimalFormat.format(investmentAssets.getBalance(),"0.##"));
					if(investmentAssets.getBalance().compareTo(new BigDecimal(0))>0) {
						resp.setInvestmentAssetsStr(investmentAssets.getBalance()+"元");//投资资产
					}
				}
				AccountBook apUndisIncome = accountBookMap.get("1201");//资产池 应收投资收益	
				if(apUndisIncome!=null) {
					resp.setApUndisIncome(ProductDecimalFormat.format(apUndisIncome.getBalance(),"0.##"));
					if(apUndisIncome.getBalance().compareTo(new BigDecimal(0))>0) {
						resp.setApUndisIncomeStr(apUndisIncome.getBalance()+"元");//资产池未分配收益
					}
				}
				AccountBook apReceiveIncome = accountBookMap.get("2201");//资产池 未分配收益
				if(apReceiveIncome!=null) {
					resp.setApReceiveIncome(ProductDecimalFormat.format(apReceiveIncome.getBalance(),"0.##"));
					if(apReceiveIncome.getBalance().compareTo(new BigDecimal(0))>0) {
						resp.setApReceiveIncomeStr(apReceiveIncome.getBalance()+"元");//应收投资收益
					}
					undisIncome = apReceiveIncome.getBalance().subtract(productRewardBenefit).subtract(productDistributionIncome);
				}
			}
			
			resp.setProductTotalScale(ProductDecimalFormat.format(productTotalScale,"0.##"));//产品总规模 王国处获取
			if(productTotalScale.compareTo(new BigDecimal(0))>0) {
				resp.setProductTotalScaleStr(productTotalScale+"元");//产品总规模
			}
			resp.setProductRewardBenefit(ProductDecimalFormat.format(productRewardBenefit,"0.##"));//奖励收益 王国处获取
			if(productRewardBenefit.compareTo(new BigDecimal(0))>0) {
				resp.setProductRewardBenefitStr(productRewardBenefit+"元");//奖励收益
			}
			
			resp.setProductDistributionIncome(ProductDecimalFormat.format(productDistributionIncome,"0.##"));//分配收益
			resp.setProductAnnualYield(ProductDecimalFormat.format(productAnnualYield,"0.##"));//年化收益率
			
			resp.setAssetpoolOid(assetpoolOid);
			List<Product> ps = productService.getProductListByAssetPoolOid(assetpoolOid);
			if(ps!=null && ps.size()>0) {
				Product p = ps.get(0);
				resp.setProductOid(p.getOid());
				resp.setIncomeCalcBasis(p.getIncomeCalcBasis());
			} else {
				resp.setIncomeCalcBasis("365");
			}
			resp.setIncomeDate(DateUtil.formatDate(DateUtil.getCurrDate().getTime()));
			
			IncomeEvent lastIncomeEvent = this.findLastIncomeEvent(assetpoolOid);//收益分配日
			//上一收益分配日
			if(lastIncomeEvent!=null) {
				resp.setLastIncomeDate(DateUtil.format(lastIncomeEvent.getBaseDate()));
				resp.setIncomeDays(DateUtil.getDaysBetween(DateUtil.getCurrDate(), new java.util.Date(lastIncomeEvent.getBaseDate().getTime()))-1);//收益分配天数
			} else {
				resp.setLastIncomeDate("");
				resp.setIncomeDays(1);//收益分配天数
			}
			resp.setIncomeDaysStr(resp.getIncomeDays()+"天");
			
			resp.setUndisIncome(ProductDecimalFormat.format(undisIncome,"0.##"));//未分配收益
			
			BigDecimal receiveIncome = new BigDecimal(0);//应收投资收益
			
			if(undisIncome.compareTo(new BigDecimal(0))<0) {
				receiveIncome = undisIncome.negate();
			}
			resp.setUndisIncome(ProductDecimalFormat.format(receiveIncome,"0.##"));//应收投资收益
			
			BigDecimal totalScale = productTotalScale.add(productRewardBenefit).add(productDistributionIncome);
			resp.setTotalScale(ProductDecimalFormat.format(totalScale,"0.##"));//产品总规模
			resp.setAnnualYield(ProductDecimalFormat.format(productAnnualYield,"0.##"));//产品年化收益率
			resp.setMillionCopiesIncome(ProductDecimalFormat.format(productAnnualYield.multiply(new BigDecimal(10000)).divide(new BigDecimal(resp.getIncomeCalcBasis())),"0.####"));//万份收益
		}
		return resp;
	}
	
	/**
	 * 查询当天是否已经有该资产池的收益分配有效记录
	 * @param assetPoolOid
	 * @return
	 */
	private IncomeEvent findTodayIncomeEvent(String assetPoolOid) {
		Specification<IncomeEvent> spec = new Specification<IncomeEvent>() {
			@Override
			public Predicate toPredicate(Root<IncomeEvent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("assetPool").get("oid").as(String.class), assetPoolOid), 
						cb.equal(root.get("baseDate").as(Date.class), new Date(new java.util.Date().getTime())));
			}
		};
		Specification<IncomeEvent> statusSpec = new Specification<IncomeEvent>() {
			@Override
			public Predicate toPredicate(Root<IncomeEvent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.or(cb.equal(root.get("status").as(String.class), IncomeEvent.STATUS_Create), 
						cb.equal(root.get("status").as(String.class), IncomeEvent.STATUS_Pass));
			}
		};
		spec = Specifications.where(spec).and(statusSpec);
		
		List<IncomeEvent> ims = incomeEventDao.findAll(spec);
		if(ims!=null && ims.size()>0) {
			 return ims.get(0);
		}
		return null;
		
	}
	
	/**
	 * 查询该资产池的最近一天的审核通过的收益分配
	 * @param assetPoolOid
	 * @return
	 */
	private IncomeEvent findLastIncomeEvent(String assetPoolOid) {
		Specification<IncomeEvent> spec = new Specification<IncomeEvent>() {
			@Override
			public Predicate toPredicate(Root<IncomeEvent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("assetPool").get("oid").as(String.class), assetPoolOid), 
						cb.lessThan(root.get("baseDate").as(Date.class), new Date(new java.util.Date().getTime())));
			}
		};
		Specification<IncomeEvent> statusSpec = new Specification<IncomeEvent>() {
			@Override
			public Predicate toPredicate(Root<IncomeEvent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("status").as(String.class), IncomeEvent.STATUS_Pass);
			}
		};
		spec = Specifications.where(spec).and(statusSpec);
		
		List<IncomeEvent> ims = incomeEventDao.findAll(spec, new Sort(new Order(Direction.DESC, "createTime")));
		if(ims!=null && ims.size()>0) {
			 return ims.get(0);
		}
		return null;
		
	}
	
	@Transactional
	public BaseResp saveIncomeAdjust(IncomeAllocateForm form, String operator) throws ParseException {
		BaseResp response = new BaseResp();
		
		IncomeEvent ie = this.findTodayIncomeEvent(form.getAssetpoolOid());
		if(ie!=null) {
			response.setErrorCode(-1);
			response.setErrorMessage("今日已经申请过收益分配");
			throw AMPException.getException(60001);
		}
		AssetPoolEntity assetPool = poolService.getByOid(form.getAssetpoolOid());
		if(assetPool==null) {
			throw AMPException.getException(60000);
		}
		List<Product> ps = productService.getProductListByAssetPoolOid(form.getAssetpoolOid());
		Product product = null;
		if(ps!=null && ps.size()>0) {
			product = ps.get(0);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		IncomeEvent incomeEvent = new IncomeEvent();
		incomeEvent.setOid(StringUtil.uuid());
		incomeEvent.setAssetPool(assetPool);
		incomeEvent.setBaseDate(new Date(now.getTime()));
		incomeEvent.setAllocateIncome(new BigDecimal(form.getProductRewardBenefit()).add(new BigDecimal(form.getProductDistributionIncome())));//总分配收益 
		incomeEvent.setCreator(operator);
		incomeEvent.setCreateTime(now);
		incomeEvent.setStatus(IncomeEvent.STATUS_Create);
		incomeEventDao.save(incomeEvent);
		
		IncomeAllocate incomeAllocate = new IncomeAllocate();
		incomeAllocate.setOid(StringUtil.uuid());
		incomeAllocate.setIncomeEvent(incomeEvent);
		incomeAllocate.setProduct(product);
		incomeAllocate.setBaseDate(incomeEvent.getBaseDate());
		incomeAllocate.setCapital(new BigDecimal(form.getProductTotalScale()));
		incomeAllocate.setAllocateIncome(new BigDecimal(form.getProductDistributionIncome()));
		incomeAllocate.setRewardIncome(new BigDecimal(form.getProductRewardBenefit()));
		incomeAllocate.setRatio(ProductDecimalFormat.divide(new BigDecimal(form.getProductAnnualYield())));
		incomeAllocateDao.save(incomeAllocate);
				
		return response;
	}
	
	public IncomeDistributionResp getIncomeAdjust(String oid) {
		IncomeAllocate incomeAllocate = incomeAllocateDao.findOne(oid);
		IncomeDistributionResp idr = new IncomeDistributionResp(incomeAllocate);
		
		Map<String, AdminObj> adminObjMap = new HashMap<String, AdminObj>();
		AdminObj adminObj = null;
		if(!StringUtil.isEmpty(idr.getCreator())) {
			if (adminObjMap.get(idr.getCreator()) == null) {
				try {
					adminObj = adminSdk.getAdmin(idr.getCreator());
					adminObjMap.put(idr.getCreator(), adminObj);
				} catch (Exception e) {
				}
			}
			if (adminObjMap.get(idr.getCreator()) != null) {
				idr.setCreator(adminObjMap.get(idr.getCreator()).getName());
			}
		}
		if(!StringUtil.isEmpty(idr.getAuditor())) {
			if (adminObjMap.get(idr.getAuditor()) == null) {
				try {
					adminObj = adminSdk.getAdmin(idr.getAuditor());
					adminObjMap.put(idr.getAuditor(), adminObj);
				} catch (Exception e) {
				}

			}
			if (adminObjMap.get(idr.getAuditor()) != null) {
				idr.setAuditor(adminObjMap.get(idr.getAuditor()).getName());
			}
		}
		
		return idr;
	}
	
	/**
	 * 资产池 收益分配列表
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public PageResp<IncomeDistributionResp> getIncomeAdjustList(Specification<IncomeAllocate> spec, Pageable pageable) {
		Page<IncomeAllocate> cas = this.incomeAllocateDao.findAll(spec, pageable);
		
		PageResp<IncomeDistributionResp> pagesRep = new PageResp<IncomeDistributionResp>();
		
		if (cas != null && cas.getContent() != null && cas.getTotalElements() > 0) {
			List<IncomeDistributionResp> rows = new ArrayList<IncomeDistributionResp>();

			Map<String, AdminObj> adminObjMap = new HashMap<String, AdminObj>();
			AdminObj adminObj = null;
			
			for (IncomeAllocate ia : cas) {
				IncomeDistributionResp idr = new IncomeDistributionResp(ia);
				
				if(!StringUtil.isEmpty(idr.getCreator())) {
					if (adminObjMap.get(idr.getCreator()) == null) {
						try {
							adminObj = adminSdk.getAdmin(idr.getCreator());
							adminObjMap.put(idr.getCreator(), adminObj);
						} catch (Exception e) {
						}
					}
					if (adminObjMap.get(idr.getCreator()) != null) {
						idr.setCreator(adminObjMap.get(idr.getCreator()).getName());
					}
				}
				if(!StringUtil.isEmpty(idr.getAuditor())) {
					if (adminObjMap.get(idr.getAuditor()) == null) {
						try {
							adminObj = adminSdk.getAdmin(idr.getAuditor());
							adminObjMap.put(idr.getAuditor(), adminObj);
						} catch (Exception e) {
						}

					}
					if (adminObjMap.get(idr.getAuditor()) != null) {
						idr.setAuditor(adminObjMap.get(idr.getAuditor()).getName());
					}
				}
				
				rows.add(idr);
			}
			pagesRep.setRows(rows);
		}
		pagesRep.setTotal(cas.getTotalElements());

		return pagesRep;
	}
	
	@Transactional
	public BaseResp auditPassIncomeAdjust(String oid, String operator) {
		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		IncomeAllocate incomeAllocate = incomeAllocateDao.findOne(oid);
		IncomeEvent ie = this.incomeEventDao.findOne(incomeAllocate.getIncomeEvent().getOid());
		ie.setAuditor(operator);
		ie.setAuditTime(now);
		ie.setStatus(IncomeEvent.STATUS_Pass);
		incomeEventDao.saveAndFlush(ie);
		
		spvDocumentService.incomeAllocate(ie.getAssetPool().getOid(), ie.getOid(), ie.getAllocateIncome());
		//调王国的
		return response;
	}
	
	@Transactional
	public BaseResp auditFailIncomeAdjust(String oid, String operator) {
		BaseResp response = new BaseResp();
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		IncomeAllocate incomeAllocate = incomeAllocateDao.findOne(oid);
		IncomeEvent ie = this.incomeEventDao.findOne(incomeAllocate.getIncomeEvent().getOid());
		ie.setAuditor(operator);
		ie.setAuditTime(now);
		ie.setStatus(IncomeEvent.STATUS_Fail);
		incomeEventDao.saveAndFlush(ie);
		
		return response;
	}
	
	@Transactional
	public IncomeAllocate deleteIncomeAdjust(String oid, String operator) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		IncomeAllocate incomeAllocate = incomeAllocateDao.findOne(oid);
		IncomeEvent ie = this.incomeEventDao.findOne(incomeAllocate.getIncomeEvent().getOid());
		ie.setAuditor(operator);
		ie.setAuditTime(now);
		ie.setStatus(IncomeEvent.STATUS_Delete);
		incomeEventDao.saveAndFlush(ie);
		return incomeAllocate;
	}

}
