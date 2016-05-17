package com.guohuai.asset.manage.boot.product;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import com.guohuai.asset.manage.boot.file.SaveFileForm;
import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;
import com.guohuai.asset.manage.component.web.parameter.validation.Regex;
import com.guohuai.asset.manage.component.web.parameter.validation.RegexRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveCurrentProductForm implements Serializable {

	private static final long serialVersionUID = -7236645696766816814L;
	
	private String oid;
	@NotBlank(message = "产品编号不能为空！")
	private String code;
	@NotBlank
	private String name;//产品简称
	@NotBlank
	private String fullName;//产品全称
	@NotBlank
	private String manager;//产品管理人简称
	@NotBlank
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
	@Digits(integer = 4, fraction = 4, message = "托管费率参数错误")
	private String manageRate;//托管费率
	@Digits(integer = 4, fraction = 4, message = "固定管理费率参数错误")
	private String fixedManageRate;//固定管理费率
	@NotBlank(message = "收益结转周期不可为空")
	private String accrualCycleOid;//收益结转周期
	private String payModeOid;//付利方式
	private int payModeDate;//付利具体几号
	@Enumerations(values = { "MANUALINPUT", "FIRSTRACKTIME" }, message = "产品成立时间类型参数错误")
	@NotBlank
	private String setupDateType;//产品成立时间类型
	@DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
	private String setupDate;//成立时间
	@Range(min = 1, max = 240, message = "起息日参数错误")
	@Digits(integer = 4, fraction = 0, message = "起息日参数错误")
	private int interestsFirstDate;//起息日:募集满额后()个自然日
	@Range(min = 1, max = 240, message = "锁定期参数错误")
	@Digits(integer = 4, fraction = 0, message = "锁定期参数错误")
	private int lockPeriod;//锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
	@Digits(integer = 4, fraction = 4, message = "预期年化收益参数错误")
	@NotBlank(message = "预期年化收益不可为空")
	private String expAror;//预期年化收益率
	@Digits(integer = 4, fraction = 2, message = "预期年化收益参数错误")
	private String expArorSec;//预期年化收益率区间
	@Digits(integer = 12, fraction = 2, message = "单位份额净值参数错误")
	@NotBlank(message = "单位份额净值不可为空")
	private String netUnitShare;//单位份额净值
	@Digits(integer = 12, fraction = 0, message = "单笔投资最低份额参数错误")
	@Min(value = 1, message = "单笔投资最低份额参数错误")
	private int investMin;//单笔投资最低份额
	@Digits(integer = 12, fraction = 0, message = "单笔投资最高份额参数错误")
	private int investMax;//单笔投资最高份额
	@Digits(integer = 12, fraction = 0, message = "单笔投资追加份额参数错误")
	private int investAdditional;//单笔投资追加份额
	@Digits(integer = 12, fraction = 0, message = "单日净赎回上限参数错误")
	@Min(value = 1, message = "单日净赎回上限参数错误")
	@NotBlank(message = "单日净赎回上限不可为空")
	private int netMaxRredeemDay;//单日净赎回上限
	@Range(min = 1, max = 240, message = "申购确认日参数错误")
	@Digits(integer = 4, fraction = 0, message = "申购确认日参数错误")
	private int purchaseConfirmDate;//申购确认日:()个
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "申购确认日类型参数错误,只能是自然日或交易日")
	private String purchaseConfirmDateType;//申购确认日类型:自然日或交易日
	@Range(min = 1, max = 240, message = "赎回确认日参数错误")
	@Digits(integer = 4, fraction = 0, message = "赎回确认日参数错误")
	private int redeemConfirmDate;//赎回确认日:()个
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "申赎回确认日类型参数错误,只能是自然日或交易日")
	private String redeemConfirmDateType;//赎回确认日类型自然日或交易日
	@Enumerations(values = { "NATRUE", "TRADE" }, message = "批量赎回定时任务参数错误,只能是每自然日或每交易日")
	private String redeemTimingTaskDateType;//批量赎回定时任务类型
	@Regex(regexRule = RegexRule.TIME, message = "批量收回的时间格式必须是HH:MM:SS！")
	@NotBlank
	private String redeemTimingTaskTime;//批量赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
	private String investComment;//投资标的
	@Length(max = 10000, message = "产品说明长度不能超过10000（包含）！")
	@NotBlank
	private String instruction;//产品说明
	@Enumerations(values = { "R1", "R2", "R3", "R4", "R5" }, message = "风险等级参数错误")
	private String riskLevel;//风险等级
	@Valid
	private List<SaveFileForm> files;//附加文件
	
}
