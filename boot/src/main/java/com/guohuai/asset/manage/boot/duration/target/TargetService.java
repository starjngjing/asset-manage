package com.guohuai.asset.manage.boot.duration.target;

import java.math.BigDecimal;
import java.util.Date;
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
import com.guohuai.asset.manage.boot.duration.order.trust.TrustEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustIncomeForm;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.boot.investment.pool.InvestmentPoolService;
import com.guohuai.asset.manage.boot.investment.pool.TargetIncome;
import com.guohuai.asset.manage.component.util.DateUtil;

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
				form.setCashtoolOid(ct.getOid());
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
				form.setTargetOid(inv.getOid());
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
				form.setFloorVolume(inv.getFloorVolume());
				form.setIncome(inv.getExpIncome());
				form.setSetDate(inv.getSetDate());
				form.setRaiseScope(inv.getRaiseScope());
				form.setLife(inv.getLife());
				form.setState(inv.getState());
				
				
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
	public CashTool getCashToolByOid(String oid) {
		
		return cashToolService.findByOid(oid);
	}

	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public FundForm getFundByOid(String oid) {
		FundForm form = new FundForm();
		
		CashTool entity = this.getCashToolByOid(oid);
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
	public Investment getInvestmentByOid(String oid) {
		
		return investmentService.getInvestmentDet(oid);
	}

	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	@Transactional
	public TrustForm getTrustByOid(String oid) {
		TrustForm form = new TrustForm();
		
		Investment entity = this.getInvestmentByOid(oid);
		if (null != entity) {
			form.setTargetOid(entity.getSn());
			form.setTargetName(entity.getName());
			form.setTargetType(entity.getType());
			form.setIncomeRate(entity.getExpAror());
			form.setSubjectRating(entity.getSubjectRating());
			form.setExpSetDate(entity.getExpSetDate());
			form.setExpAror(entity.getExpAror());
			form.setAccrualType(entity.getAccrualType());
			form.setArorFirstDate(entity.getArorFirstDate());
			form.setContractDays(entity.getContractDays());
			form.setRaiseScope(entity.getRaiseScope());
			form.setLife(entity.getLife());
			form.setFloorVolume(entity.getFloorVolume());
			form.setCollectStartDate(entity.getCollectStartDate());
			form.setCollectEndDate(entity.getCollectEndDate());
			form.setCollectIncomeRate(entity.getCollectIncomeRate());
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
	
	/**
	 * 本息兑付数据
	 * @param target
	 * 				标的对象
	 * @param targetOid
	 * @param entity
	 * 				持仓对象
	 * @return
	 */
	public List<TrustIncomeForm> getIncomeData(Investment target, TrustEntity entity) {
		List<TrustIncomeForm> formList = Lists.newArrayList();
		List<TargetIncome> list = investmentPoolService.getTargetIncome(target.getOid());
		// 日利息 算法：利息=本金×收益率%÷365天(有时是360天)
		BigDecimal day_yield = new BigDecimal(0);
		TrustIncomeForm form = null;
		if (target.getContractDays() != 0)
			day_yield = entity.getInvestAmount()
					.multiply(entity.getTarget().getExpAror().divide(TrustIncomeForm.NUM100))
					.divide(new BigDecimal(target.getContractDays()), 4, BigDecimal.ROUND_HALF_UP);
		
		// 获取付息方式 OTP:一次性；CYCLE:周期
		String pay_mode = target.getAccrualType();
		if ("ACCRUALTYPE_05".equals(pay_mode)) {
			form = new TrustIncomeForm();
			// 付息日
			form.setIncomeDate(new java.sql.Date(DateUtil.addDay(target.getExpSetDate(), target.getLifed()).getTime()));
			// 本金
			form.setCapital(entity.getInvestAmount());
			// 预期利益	算法：日利息*实际存续天数
			form.setExpIncome(new BigDecimal(target.getLifed()).multiply(day_yield).setScale(4, BigDecimal.ROUND_HALF_UP));
			form.setExpIncomeRate(target.getExpAror());
			// 实际值
			form.setIncome(form.getExpIncome());
			form.setIncomeRate(form.getExpIncomeRate());
			form.setSeq(1);
			
			formList.add(form);
		} else {
			// 设置周期递增基数
			int addNum = 0;
			// 获取付息周期 M:月；Q:季；H_Y:半年；Y:年
			if ("ACCRUALTYPE_01".equals(pay_mode)) {
				addNum = 1;
			} else if ("ACCRUALTYPE_02".equals(pay_mode)) {
				addNum = 3;
			} else if ("ACCRUALTYPE_03".equals(pay_mode)) {
				addNum = 6;
			} else if ("ACCRUALTYPE_04".equals(pay_mode)) {
				addNum = 12;
			}
			// 付息日
			Date sdate = null;
			// 获取付息周期的方式 NATURAL:自然；CONTRACT:合同
			if ("CONTRACT_YEAR".equals(target.getAccrualCycleType())) {
				// 首付日（收益分配基准日）算法：首付日自动为“收益起始日”+“周期”
				sdate = DateUtil.lastDate(
						DateUtil.addMonth(
								new java.util.Date(target.getIncomeStartDate().getTime()), addNum));
			} else {
				sdate = target.getArorFirstDate();
			}
			// 剩余结算天数
			int pay_days = target.getLifed();
			// 当天月天数
			int curr_month_days = 0;
			
			do {
				int seq = 1;
				form = new TrustIncomeForm();
				form.setCapital(entity.getInvestAmount());
				form.setIncomeDate(sdate);
				// 判断剩余结算天数是否满足当天月，如果不满足，则以剩余结算天数进行收益结算
				curr_month_days = DateUtil.getDaysOfMonth(new java.util.Date(target.getIncomeStartDate().getTime()));
				if (pay_days > curr_month_days) {
					form.setExpIncome(new BigDecimal(curr_month_days)
							.multiply(day_yield).setScale(4, BigDecimal.ROUND_HALF_UP));
					pay_days = pay_days - curr_month_days;
				} else {
					form.setExpIncome(new BigDecimal(pay_days)
							.multiply(day_yield).setScale(4, BigDecimal.ROUND_HALF_UP));
				}
				// 收益基准日=收益截止日+1
				if (DateUtil.compare_date(target.getIncomeEndDate(), sdate) == 0)
					sdate = DateUtil.addDay(sdate, 1);
				
				form.setExpIncomeRate(target.getExpAror());
				form.setIncome(form.getExpIncome());
				form.setIncomeRate(form.getExpIncomeRate());
				sdate = DateUtil.addMonth(sdate, addNum);
				if (DateUtil.compare_date(target.getIncomeEndDate(), sdate) < 0) {
					sdate = new Date(target.getIncomeEndDate().getTime());
				}
				form.setSeq(seq);
				
				formList.add(form);
			} while (DateUtil.compare_date(target.getIncomeEndDate(), sdate) >= 0);
		}
		
		if (null != list && list.size() > 0) {
			for (TargetIncome in : list) {
				form = new TrustIncomeForm();
				form = formList.get(in.getSeq());
				form.setExpIncome(in.getIncome());
				form.setExpIncomeRate(in.getIncomeRate());
				form.setIncome(in.getIncome());
				form.setIncomeRate(in.getIncomeRate());
			}
		}
		
		return formList;
	}
}
