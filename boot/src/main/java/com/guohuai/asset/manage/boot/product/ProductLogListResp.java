package com.guohuai.asset.manage.boot.product;

import java.util.List;

import com.guohuai.asset.manage.boot.file.FileResp;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductLogListResp extends BaseResp {

	public ProductLogListResp(Product p) {
		this.oid = p.getOid();
		this.code = p.getCode();
		this.name = p.getName();
		this.fullName = p.getFullName();
		this.administrator = p.getAdministrator();// 管理人
		if (null != p.getType()) {
			this.typeOid = p.getType().getOid();// 产品类型
			this.typeName = p.getType().getName();
		}
		if (p.getAssetPool() != null) {
			this.assetPoolOid = p.getAssetPool().getOid();
			this.assetPoolName = p.getAssetPool().getName();
		}
		this.raiseStartDate = p.getRaiseStartDate() != null ? DateUtil.formatDate(p.getRaiseStartDate().getTime()) : "";// 募集开始时间
		this.raisePeriod = p.getRaisePeriodDays();// 募集期:()个自然日
		this.interestsFirstDate = p.getInterestsFirstDays();// 起息日:募集满额后()个自然日
		this.durationPeriod = p.getDurationPeriodDays();// 存续期:()个自然日
		this.expAror = ProductDecimalFormat.format(ProductDecimalFormat.multiply(p.getExpAror()));// 预期年化收益率
		this.expArorSec = ProductDecimalFormat.format(ProductDecimalFormat.multiply(p.getExpArorSec()));// 预期年化收益率区间
		this.raisedTotalNumber = p.getRaisedTotalNumber();// 募集总份额
		this.netUnitShare = p.getNetUnitShare().toPlainString();// 单位份额净值
		this.investMin = p.getInvestMin();// 单笔投资最低份额
		this.investMax = p.getInvestMax();// 单笔投资最高份额
		this.investAdditional = p.getInvestAdditional();// 单笔投资追加份额
		this.netMaxRredeemDay = p.getNetMaxRredeemDay();// 单日净赎回上限
		this.minRredeem = p.getMinRredeem();
		this.accrualCycleOid = p.getAccrualCycleOid();
		this.purchaseConfirmDate = p.getPurchaseConfirmDays();// 申购确认日:()个
		this.purchaseConfirmDateType = p.getPurchaseConfirmDaysType();// 申购确认日类型:自然日或交易日
		this.redeemConfirmDate = p.getRedeemConfirmDays();// 赎回确认日:()个
		this.redeemConfirmDateType = p.getRedeemConfirmDaysType();// 赎回确认日类型:自然日或交易日
		this.redeemTimingTaskDateType = p.getRedeemTimingTaskDaysType();// 赎回定时任务类型:自然日或交易日
		this.redeemTimingTaskTime = p.getRedeemTimingTaskTime() != null ? DateUtil.format(p.getRedeemTimingTaskTime().getTime(), DateUtil.timePattern) : "";// 赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
		this.accrualDate = p.getAccrualRepayDays();// 还本付息日 存续期结束后第()个自然日
		this.investComment = p.getInvestComment();// 投资标的
		this.instruction = p.getInstruction();// 产品说明
		this.riskLevel = p.getRiskLevel();// 风险等级
		this.fileKeys = p.getFileKeys();// 附加文件
		this.status = p.getState();// 产品状态
		this.createTime = DateUtil.formatDate(p.getCreateTime().getTime());// 创建时间
		this.updateTime = DateUtil.formatDate(p.getUpdateTime().getTime());// 更新时间
		this.operator = p.getOperator();// 操作员
		this.auditState = p.getAuditState();// 审核状态
		this.setupDate = p.getSetupDate() != null ? DateUtil.formatDate(p.getSetupDate().getTime()) : "";// 产品成立时间（存续期开始时间）
		this.isOpenPurchase = p.getIsOpenPurchase();// 开放申购期
		this.isOpenRemeed = p.getIsOpenRemeed();// 开放赎回期

		this.purchaseApplyStatus = p.getPurchaseApplyStatus();
		this.redeemApplyStatus = p.getRedeemApplyStatus();
		this.maxSaleVolume = p.getMaxSaleVolume();
	}

	private String oid;
	private String code;
	private String name;// 产品名称
	private String fullName;// 产品全称
	private String administrator;// 管理人
	private String typeOid;// 产品类型
	private String typeName;// 产品类型
	private String assetPoolOid;// 资产池Oid
	private String assetPoolName;// 资产池名称
	private String raiseStartDate;// 募集开始时间
	private Integer raisePeriod;// 募集期:()个自然日
	private Integer interestsFirstDate;// 起息日:募集满额后()个自然日
	private Integer durationPeriod;// 存续期:()个自然日
	private String expAror;// 预期年化收益率
	private String expArorSec;// 预期年化收益率区间
	private BigDecimal raisedTotalNumber;// 募集总份额
	private String netUnitShare;// 单位份额净值
	private BigDecimal investMin;// 单笔投资最低份额
	private BigDecimal investMax;// 单笔投资最高份额
	private BigDecimal investAdditional;// 单笔投资追加份额
	private BigDecimal netMaxRredeemDay;// 单日净赎回上限
	private BigDecimal minRredeem;// 单笔净赎回下限
	private String accrualCycleOid;// 收益结转周期
	private String accrualCycleName;// 收益结转周期
	private Integer purchaseConfirmDate;// 申购确认日:()个
	private String purchaseConfirmDateType;// 申购确认日类型:自然日或交易日
	private Integer redeemConfirmDate;// 赎回确认日:()个
	private String redeemConfirmDateType;// 赎回确认日类型:自然日或交易日
	private String redeemTimingTaskDateType;// 赎回定时任务类型:自然日或交易日
	private String redeemTimingTaskTime;// 赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
	private Integer accrualDate;// 还本付息日 存续期结束后第()个自然日
	private String investComment;// 投资标的
	private String instruction;// 产品说明
	private String riskLevel;// 风险等级
	private String fileKeys;// 附加文件
	private String status;// 产品状态
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String operator;// 操作员
	private String auditState;// 审核状态
	private String auditComment;// 审核备注
	private String setupDate;// 产品成立时间（存续期开始时间）
	private String isOpenPurchase;// 开放申购期
	private String isOpenRemeed;// 开放赎回期
	private Integer channelNum;// 取到数
	private List<FileResp> files;
	private String applicant;// 申请人
	private String applyTime;// 申请时间
	private String auditor;// 审核人
	private String auditTime;// 审核时间
	private String reviewer;// 复核人
	private String reviewTime;// 复核时间
	private String accesser; // 准入人
	private String accessTime; // 准入时间
	private String purchaseApplyStatus;
	private String redeemApplyStatus;
	private BigDecimal maxSaleVolume;

}
