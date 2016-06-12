package com.guohuai.asset.manage.boot.product;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;
import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveCurrentProductForm implements Serializable {

	private static final long serialVersionUID = -7236645696766816814L;
	
	private String oid;
	private String code;
	private String name;//产品简称
	private String fullName;//产品全称
	private String administrator;//管理人
	@NotBlank
	private String typeOid;//产品类型
	@NotBlank
	@Enumerations(values = { "YES", "NO" }, message = "额外增信参数错误,只能是有或无")
	private String reveal;//额外增信
	@Enumerations(values = { "CNY", "USD", "EUR", "JPY", "GBP", "HKD", "SGD", "JMD", "AUD", "CHF" }, message = "产品币种类型参数错误")
	@NotBlank
	private String currency;//币种
	private String incomeCalcBasis;//收益计算基础
	private String manageRate;//托管费率
	private String fixedManageRate;//固定管理费率
	@NotBlank(message = "收益结转周期不可为空")
	private String accrualCycleOid;//收益结转周期
	private String payModeOid;//付利方式
	private String payModeDate;//付利具体几号
	@Enumerations(values = { "MANUALINPUT", "FIRSTRACKTIME" }, message = "产品成立时间类型参数错误")
	@NotBlank
	private String setupDateType;//产品成立时间类型
	private String setupDate;//成立时间
	private String interestsDate;//起息日:募集满额后()个自然日
	private String lockPeriod;//锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
	private String expAror;//预期年化收益率
	private String expArorSec;//预期年化收益率区间
	private String netUnitShare;//单位份额净值
	private String investMin;//单笔投资最低份额
	private String investMax;//单笔投资最高份额
	private String purchaseLimit;//申购上限
	private String investAdditional;//单笔投资追加份额
	private String netMaxRredeemDay;//单日净赎回上限
	private String minRredeem;//单笔净赎回下限
	private String purchaseConfirmDate;//申购确认日:()个
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "申购确认日类型参数错误,只能是自然日或交易日")
	private String purchaseConfirmDateType;//申购确认日类型:自然日或交易日
	private String redeemConfirmDate;//赎回确认日:()个
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "申赎回确认日类型参数错误,只能是自然日或交易日")
	private String redeemConfirmDateType;//赎回确认日类型自然日或交易日
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "批量赎回定时任务参数错误,只能是每自然日或每交易日")
	private String redeemTimingTaskDateType;//批量赎回定时任务类型
	private String investComment;//投资标的
	private String instruction;//产品说明
	@Enumerations(values = { "R1", "R2", "R3", "R4", "R5" }, message = "风险等级参数错误")
	private String riskLevel;//风险等级
	private String files;//附加文件
	private String assetPoolOid;//资产池
	private String investFile;//投资协议书
	private String revealComment;//增信备注
	private String[] channels;//渠道
	
}
