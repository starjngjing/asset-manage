package com.guohuai.asset.manage.boot.duration.fact.income;


import com.guohuai.asset.manage.component.web.view.BaseResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class IncomeAllocateCalcResp extends BaseResp {

	public String oid;
	public String assetpoolOid;
	public String productOid;
	public String investmentAssets;//投资资产
	public String apUndisIncome;//资产池未分配收益
	public String apReceiveIncome;//应收投资收益
	public String lastIncomeDate;//上一收益分配日
	public String incomeDate;//收益分配日
	public int incomeDays;//收益分配天数
	public String productTotalScale;//产品总规模
	public String productRewardBenefit;//奖励收益
	public String productDistributionIncome;//分配收益
	public String productAnnualYield;//年化收益率
	public String undisIncome;//未分配收益
	public String receiveIncome;//应收投资收益
	public String totalScale;//产品总规模
	public String annualYield;//产品年化收益率
	public String millionCopiesIncome;//万份收益
	private String incomeCalcBasis;//收益计算基础
	
	public String investmentAssetsStr;//投资资产
	public String apUndisIncomeStr;//资产池未分配收益
	public String apReceiveIncomeStr;//应收投资收益
	public String incomeDaysStr;//收益分配天数
	public String productTotalScaleStr;//产品总规模
	public String productRewardBenefitStr;//奖励收益
	
	
}
