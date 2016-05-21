package com.guohuai.asset.manage.boot.product;

import java.util.List;
import com.guohuai.asset.manage.boot.file.FileResp;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
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
public class ProductDetailResp extends BaseResp {

	public ProductDetailResp(Product p) {
		this.oid = p.getOid();
		this.code = p.getCode();
		this.name = p.getName();
		this.fullName = p.getFullName();
		this.administrator = p.getAdministrator();//管理人
		if (null != p.getType()) {
			this.typeOid = p.getType().getOid();//产品类型
			this.typeName = p.getType().getName();
		}
		
		this.reveal = p.getReveal();//额外增信
		this.currency = p.getCurrency();//币种
		this.incomeCalcBasis = p.getIncomeCalcBasis();//收益计算基础
		this.manageRate = p.getManageRate().toPlainString();//托管费率
		this.fixedManageRate = p.getFixedManageRate().toPlainString();//固定管理费率
		if(p.getAssetPool()!=null) {
			this.investmentOid =  p.getAssetPool().getOid();
			this.investment = p.getAssetPool().getName();
		}
		this.raiseStartDate = p.getRaiseStartDate()!=null?DateUtil.formatDate(p.getRaiseStartDate().getTime()):"";//募集开始时间
		this.raiseStartDateType = p.getRaiseStartDateType();
		this.raisePeriod = p.getRaisePeriod();//募集期:()个自然日
		this.interestsFirstDate = p.getInterestsFirstDate();//起息日:募集满额后()个自然日
		this.interestsDate = p.getInterestsFirstDate();//起息日:募集满额后()个自然日
		this.durationPeriod = p.getDurationPeriod();//存续期:()个自然日
		this.expAror = p.getExpAror().toPlainString();//预期年化收益率
		this.expArorSec = p.getExpArorSec().toPlainString();//预期年化收益率区间
		if(p.getExpAror()!=null) {
			if(p.getExpAror()!=p.getExpArorSec()) {
				this.expectedRate = p.getExpAror().toPlainString()+"%~"+p.getExpArorSec().toPlainString()+"%";
			} else {
				this.expectedRate = p.getExpAror().toPlainString()+"%";
			}
		}
		this.raisedTotalNumber = p.getRaisedTotalNumber();//募集总份额
		this.accrualDate = p.getAccrualDate();//还本付息日 存续期结束后第()个自然日
		this.setupDate = p.getSetupDate()!=null?DateUtil.formatDate(p.getSetupDate().getTime()):"";//产品成立时间（存续期开始时间）
		this.setupDateType = p .getSetupDateType();
		
		this.accrualCycleOid = p.getAccrualCycleOid();
		
		this.accrualCycleName = (StringUtil.isEmpty(p.getAccrualCycleOid())==true?"":ProductEnum.enums.get(p.getAccrualCycleOid()));//
		this.payModeOid =  p.getPayModeOid();
		this.payModeName = (StringUtil.isEmpty(p.getPayModeOid())==true?"":ProductEnum.enums.get(p.getPayModeOid()));
		this.payModeDate = p.getPayModeDate();
		this.lockPeriod = p.getLockPeriod();
		this.purchaseConfirmDate = p.getPurchaseConfirmDate();//申购确认日:()个
		this.purchaseConfirmDateType = p.getPurchaseConfirmDateType();//申购确认日类型:自然日或交易日
		this.redeemConfirmDate = p.getRedeemConfirmDate();//赎回确认日:()个
		this.redeemConfirmDateType = p.getRedeemConfirmDateType();//赎回确认日类型:自然日或交易日
		this.netMaxRredeemDay = p.getNetMaxRredeemDay();//单日净赎回上限
		this.minRredeem = p.getMinRredeem();
		this.redeemTimingTaskDateType = p.getRedeemTimingTaskDateType();//赎回定时任务类型:自然日或交易日
		this.redeemTimingTaskTime = p.getRedeemTimingTaskTime()!=null?DateUtil.format(p.getRedeemTimingTaskTime().getTime(), DateUtil.timePattern):"";//赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
		
		this.investMin = p.getInvestMin();//单笔投资最低份额
		this.investMax = p.getInvestMax();//单笔投资最高份额
		this.purchaseLimit = p.getPurchaseLimit();//申购上限
		this.investAdditional = p.getInvestAdditional();//单笔投资追加份额
		this.netUnitShare = p.getNetUnitShare().toPlainString();//单位份额净值
		this.investComment = p.getInvestComment();//投资标的
		this.instruction = p.getInstruction();//产品说明
		this.riskLevel = p.getRiskLevel();//风险等级
		this.fileKeys = p.getFileKeys();//附加文件
		this.status = p.getStatus();//产品状态
		this.createTime = DateUtil.formatDate(p.getCreateTime().getTime());//创建时间
		this.updateTime = DateUtil.formatDate(p.getUpdateTime().getTime());//更新时间
		this.operator = p.getOperator();//操作员
		this.auditState = p.getAuditState();//审核状态
		this.isOpenPurchase = p.getIsOpenPurchase();//开放申购期
		this.isOpenRemeed = p.getIsOpenRemeed();//开放赎回期
		this.publisher = p.getPublisher();//发行人
		this.currentVolume = p.getCurrentVolume();//当前份额
		this.collectedVolume = p.getCollectedVolume();//已募份额
		this.purchaseNum = p.getPurchaseNum();//已投次数
		this.lockCollectedVolume = p.getLockCollectedVolume();//锁定已募份额
		this.raiseEndDate = p.getRaiseEndDate()!=null?DateUtil.formatDate(p.getRaiseEndDate().getTime()):"";//募集结束时间
		this.raiseFailDate = p.getRaiseFailDate()!=null?DateUtil.formatDate(p.getRaiseFailDate().getTime()):"";//募集宣告失败时间
		this.durationPeriodEndDate = p.getDurationPeriodEndDate()!=null?DateUtil.formatDate(p.getDurationPeriodEndDate().getTime()):"";//存续期结束时间
		this.accrualLastDate = p.getAccrualLastDate()!=null?DateUtil.formatDate(p.getAccrualLastDate().getTime()):"";//到期还款时间
		this.endDate = p.getEndDate()!=null?DateUtil.formatDate(p.getEndDate().getTime()):"";//产品清算（结束）时间
		this.durationRepaymentTime = p.getDurationRepaymentTime()!=null?DateUtil.formatDate(p.getDurationRepaymentTime().getTime()):"";//存续期内还款时间
		this.stems = p.getStems();//来源
		
		this.riskLevelStr = (StringUtil.isEmpty(p.getRiskLevel())==true?"":ProductEnum.enums.get(p.getRiskLevel()));//风险等级
		this.revealStr = (StringUtil.isEmpty(p.getReveal())==true?"":ProductEnum.enums.get(p.getReveal()));//额外增信
		this.currencyStr = (StringUtil.isEmpty(p.getCurrency())==true?"":ProductEnum.enums.get(p.getCurrency()));
		this.incomeCalcBasisStr = (StringUtil.isEmpty(p.getIncomeCalcBasis())==true?"":p.getIncomeCalcBasis()+"(天)");//收益计算基础
		this.manageRateStr = p.getManageRate().toPlainString()+"%每年";//托管费率
		this.fixedManageRateStr = p.getFixedManageRate().toPlainString()+"%每年";//固定管理费率
		if("FIRSTRACKTIME".equals(p.getRaiseStartDateType()) && "".equals(this.raiseStartDate)) {
			this.raiseStartDateStr = "与首次上架时间同时";
		} else {
			this.raiseStartDateStr = this.raiseStartDate;
		}
		
		this.raisePeriodStr = p.getRaisePeriod()>0?p.getRaisePeriod()+"个自然日":"";//募集期:()个自然日
		
		if("PRODUCTTYPE_01".equals(this.typeOid)) {
			this.interestsFirstDateStr = p.getInterestsFirstDate()>0?"募集满额后第"+p.getInterestsFirstDate()+"个自然日":"";//起息日:募集满额后()个自然日
		} else {
			this.interestsDateStr = p.getInterestsFirstDate()>0?"成立后第"+p.getInterestsFirstDate()+"个自然日":"";//起息日:募集满额后()个自然日
		}
		this.durationPeriodStr = p.getDurationPeriod()>0?p.getDurationPeriod()+"个自然日":"";//存续期:()个自然日
		this.raisedTotalNumberStr = p.getRaisedTotalNumber()>0?p.getRaisedTotalNumber()+"份":"";//募集总份额
		this.accrualDateStr = p.getAccrualDate()>0?"存续期结束后第"+p.getAccrualDate()+"个自然日":"";
		this.lockPeriodStr = p.getLockPeriod()>0?"一旦申购，将冻结此金额"+p.getLockPeriod()+"个自然日":"";
		this.purchaseConfirmDateStr = p.getPurchaseConfirmDate()>0?
				("申购订单确认日后"+p.getPurchaseConfirmDate()+"个"     +((StringUtil.isEmpty(p.getPurchaseConfirmDateType())==true?"":ProductEnum.enums.get(p.getPurchaseConfirmDateType())))     +"内") :"";
			
		this.redeemConfirmDateStr = p.getRedeemConfirmDate()>0?
				("赎回订单确认日"+p.getRedeemConfirmDate()+"个"     +( (StringUtil.isEmpty(p.getRedeemConfirmDateType())==true?"":ProductEnum.enums.get(p.getRedeemConfirmDateType())) )+     "内"):"";
		
		this.netMaxRredeemDayStr = p.getNetMaxRredeemDay()>0?p.getNetMaxRredeemDay()+"份":"";
		this.minRredeemStr = p.getMinRredeem()>0?p.getMinRredeem()+"份":"";
		this.redeemTimingTaskTimeStr = !"".equals(this.redeemTimingTaskTime)?"每"   +(StringUtil.isEmpty(p.getRedeemTimingTaskDateType())==true?"":ProductEnum.enums.get(p.getRedeemTimingTaskDateType()))+   ""+this.redeemTimingTaskTime+"发起代付指令":"";
		this.investMinStr = p.getInvestMin()>0?p.getInvestMin()+"份":"";
		this.investMaxStr = p.getInvestMax()>0?p.getInvestMax()+"份":"";
		this.purchaseLimitStr = p.getPurchaseLimit()>0?p.getPurchaseLimit()+"份":"";
		this.investAdditionalStr = p.getInvestAdditional()>0?p.getInvestAdditional()+"份":"";
		this.netUnitShareStr = !"".equals(this.netUnitShare)?this.netUnitShare+"元":"";
		if(!"DAY".equals(this.payModeOid)) {
			this.payModeNameStr = p.getPayModeDate()>0?this.payModeName+"第"+p.getPayModeDate()+"天":"";
		}
		if("FIRSTRACKTIME".equals(p.getSetupDateType()) && "".equals(this.setupDate)) {
			this.setupDateStr = "与首次上架时间同时";
		} else {
			this.setupDateStr = this.setupDate;
		}
		
	}

	
	private String oid;
	private String code;
	private String name;//产品名称
	private String fullName;//产品全称
	private String administrator;//管理人
	private String typeOid;//产品类型
	private String typeName;//产品类型
	private String reveal;//额外增信
	private String currency;//币种
	private String incomeCalcBasis;//收益计算基础
	private String manageRate;//托管费率
	private String fixedManageRate;//固定管理费率
	private String investmentOid;//资产池Oid
	private String investment;//资产池名称
	private String raiseStartDate;//募集开始时间
	private String raiseStartDateType;//募集开始时间类型
	private int raisePeriod;//募集期:()个自然日
	private int interestsFirstDate;//起息日:募集满额后()个自然日
	private int durationPeriod;//存续期:()个自然日
	private String expectedRate;//预期年化收益率
	private String expAror;//预期年化收益率
	private String expArorSec;//预期年化收益率区间
	private int raisedTotalNumber;//募集总份额
	private int accrualDate;//还本付息日 存续期结束后第()个自然日
	private String setupDate;//产品成立时间（存续期开始时间）
	private String setupDateType;//产品成立时间类型
	private String accrualCycleOid;//收益结转周期
	private String accrualCycleName;//收益结转周期
	private String payModeOid;//付利方式
	private String payModeName;//付利方式
	private int payModeDate;//付利具体几号
	private int lockPeriod;//锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
	private int purchaseConfirmDate;//申购确认日:()个
	private String purchaseConfirmDateType;//申购确认日类型:自然日或交易日
	private int redeemConfirmDate;//赎回确认日:()个
	private String redeemConfirmDateType;//赎回确认日类型:自然日或交易日
	private int netMaxRredeemDay;//单日净赎回上限
	private int minRredeem;//单笔净赎回下限
	private String redeemTimingTaskDateType;//赎回定时任务类型:自然日或交易日
	private String redeemTimingTaskTime;//赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
	private int investMin;//单笔投资最低份额
	private int investMax;//单笔投资最高份额
	private int purchaseLimit;//申购上限
	private int investAdditional;//单笔投资追加份额
	private String netUnitShare;//单位份额净值
	private String investComment;//投资标的
	private String instruction;//产品说明
	private String riskLevel;//风险等级
	private String fileKeys;//附加文件
	private String status;//产品状态
	private String createTime;//创建时间
	private String updateTime;//更新时间
	private String operator;//操作员
	private String auditState;//审核状态
	private String isOpenPurchase;//开放申购期
	private String isOpenRemeed;//开放赎回期
	private List<FileResp> files;
	
	private int redeemTimingTaskDate;//赎回定时任务天数	 默认每日
	private String publisher;//发行人
	private int currentVolume;//当前份额
	private int collectedVolume;//已募份额
	private int purchaseNum;//已投次数
	private int lockCollectedVolume;//锁定已募份额
	
	private String raiseEndDate;//募集结束时间
	private String raiseFailDate;//募集宣告失败时间
	private String durationPeriodEndDate;//存续期结束时间
	private String accrualLastDate;//到期还款时间
	private String endDate;//产品清算（结束）时间
	private String durationRepaymentTime;//存续期内还款时间
	private String stems;//来源
	private String isDeleted;
	
	private String riskLevelStr;//风险等级
	private String revealStr;//额外增信
	private String currencyStr;//币种
	private String incomeCalcBasisStr;//收益计算基础
	private String manageRateStr;//托管费率
	private String fixedManageRateStr;//固定管理费率
	private String raiseStartDateStr;//募集开始时间类型
	private String raisePeriodStr;//募集期:()个自然日
	private String interestsFirstDateStr;//起息日:募集满额后()个自然日
	private int interestsDate;//起息日:募集满额后()个自然日
	private String interestsDateStr;//起息日:募集满额后()个自然日
	private String durationPeriodStr;//存续期:()个自然日
	private String raisedTotalNumberStr;
	private String accrualDateStr;
	private String setupDateStr;//产品成立时间（存续期开始时间）
	private String payModeNameStr;//付利方式
	private String lockPeriodStr;//锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
	private String purchaseConfirmDateStr;//申购确认日类型:自然日或交易日
	private String redeemConfirmDateStr;//赎回确认日类型:自然日或交易日
	private String netMaxRredeemDayStr;//单日净赎回上限
	private String minRredeemStr;//单笔净赎回下限
	private String redeemTimingTaskTimeStr;
	private String investMinStr;//单笔投资最低份额
	private String investMaxStr;//单笔投资最高份额
	private String purchaseLimitStr;//申购上限
	private String investAdditionalStr;//单笔投资追加份额
	private String netUnitShareStr;//单位份额净值

}

