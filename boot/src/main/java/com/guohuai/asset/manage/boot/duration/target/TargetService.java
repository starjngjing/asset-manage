package com.guohuai.asset.manage.boot.duration.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.boot.duration.order.FundForm;
import com.guohuai.asset.manage.boot.duration.order.TrustForm;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentService;

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

	/**
	 * 根据 oid 获取 货币基金（现金类管理工具） 详情
	 * @param oid
	 * @return
	 */
	public FundForm getFundByOid(String oid) {
		FundForm form = new FundForm();
		
		CashTool entity = cashToolService.findByOid(oid);
		if (null != entity) {
			form.setAssetPoolCashtoolOid(entity.getTicker());
			form.setName(entity.getSecShortName());
			form.setType(entity.getEtfLof());
			form.setYearYield7(entity.getWeeklyYield());
			form.setNetRevenue(entity.getDailyProfit());
		}
		
		return form;
	}

	/**
	 * 根据 oid 获取 信托（计划） 详情
	 * @param oid
	 * @return
	 */
	public TrustForm getTrustByOid(String oid) {
		TrustForm form = new TrustForm();
		
		Investment entity = investmentService.getInvestmentDet(oid);
		if (null != entity) {
			form.setTargetOid(entity.getSn());
		}
		
		return form;
	}
}
