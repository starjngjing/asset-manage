package com.guohuai.asset.manage.boot.duration.target;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.boot.cashtool.pool.CashtoolPoolService;
import com.guohuai.asset.manage.boot.duration.order.FundForm;
import com.guohuai.asset.manage.boot.duration.order.TrustForm;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.boot.investment.pool.InvestmentPoolService;

/**
 * 存续期--产品服务接口
 * @author star.zhu
 * 2016年5月17日
 */
@Service
public class TargetService {
	
	@Autowired
	private InvestmentService investmentService;
	@Autowired
	private CashToolService cashToolService;
	@Autowired
	private InvestmentPoolService investmentPoolService;
	@Autowired
	private CashtoolPoolService cashtoolPoolService;
	
	/**
	 * 获取可申购的现金标的
	 * @param projectTypes
	 * @return
	 */
	@Transactional
	public List<FundForm> getFundListByScopes(String[] scopes) {
		List<FundForm> formList = Lists.newArrayList();
		List<CashTool> list = cashtoolPoolService.getCollecting(scopes);
		if (!list.isEmpty()) {
			FundForm form = null;
			for (CashTool ct : list) {
				form = new FundForm();
				form.setCashtoolOid(ct.getTicker());
				form.setCashtoolName(ct.getSecShortName());
				form.setCashtoolType(ct.getEtfLof());
				form.setNetRevenue(ct.getDailyProfit());
				form.setYearYield7(ct.getWeeklyYield());
				form.setRiskLevel(ct.getRiskLevel());
				form.setDividendType(ct.getDividendType());
				form.setCirculationShares(ct.getCirculationShares());
				
				formList.add(form);
			}
		}
		
		return formList;
	}
	
	/**
	 * 获取可申购的信托标的
	 * @param projectTypes
	 * @return
	 */
	@Transactional
	public List<TrustForm> getTrustListByScopes(String[] scopes) {
		List<TrustForm> formList = Lists.newArrayList();
		List<Investment> list = investmentPoolService.getCollecting(scopes);
		if (!list.isEmpty()) {
			TrustForm form = null;
			for (Investment inv : list) {
				form = new TrustForm();
				form.setTargetOid(inv.getSn());
				form.setTargetName(inv.getName());
				form.setTargetType(inv.getType());
				form.setExpSetDate(inv.getExpSetDate());
				form.setExpAror(inv.getExpAror());
				form.setAccrualType(inv.getAccrualType());
				form.setArorFirstDate(inv.getArorFirstDate());
				form.setAccrualDate(inv.getAccrualDate());
				form.setContractDays(inv.getContractDays());
				form.setSubjectRating(inv.getSubjectRating());
				form.setCollectStartDate(inv.getCollectStartDate());
				form.setCollectEndDate(inv.getCollectEndDate());
				form.setCollectIncomeRate(inv.getCollectIncomeRate());
				
				formList.add(form);
			}
		}
		
		return formList;
	}

	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundForm getFundByOid(String oid) {
		FundForm form = new FundForm();
		
		CashTool entity = cashToolService.findByOid(oid);
		if (null != entity) {
			form.setCashtoolOid(entity.getTicker());
			form.setCashtoolName(entity.getSecShortName());
			form.setCashtoolType(entity.getEtfLof());
			form.setYearYield7(entity.getWeeklyYield());
			form.setNetRevenue(entity.getDailyProfit());
			form.setRiskLevel(entity.getRiskLevel());
			form.setDividendType(entity.getDividendType());
			form.setCirculationShares(entity.getCirculationShares());
		}
		
		return form;
	}

	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustForm getTrustByOid(String oid) {
		TrustForm form = new TrustForm();
		
		Investment entity = investmentService.getInvestmentDet(oid);
		if (null != entity) {
			form.setTargetOid(entity.getSn());
			form.setTargetName(entity.getName());
			form.setTargetType(entity.getType());
			form.setIncomeRate(entity.getExpAror());
		}
		
		return form;
	}

	/**
	 * 根据 标的名称 获取 信托（计划） 列表（支持模糊查询）
	 * @param oid
	 * @return
	 */
	@Transactional
	public List<FundForm> getTrustListByName(String name) {
		List<FundForm> formList = Lists.newArrayList();
		
//		CashTool entity = cashToolService.findByOid(name);
//		if (null != entity) {
//			form.setAssetPoolCashtoolOid(entity.getTicker());
//			form.setName(entity.getSecShortName());
//			form.setType(entity.getEtfLof());
//			form.setYearYield7(entity.getWeeklyYield());
//			form.setNetRevenue(entity.getDailyProfit());
//		}
		
		return formList;
	}
}
