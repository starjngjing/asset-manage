package com.guohuai.asset.manage.boot.product;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import com.guohuai.asset.manage.boot.file.SaveFileForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePeriodicProductForm implements Serializable {

	private static final long serialVersionUID = 6251184821616489757L;

	private String oid;
	@NotNull
	@NotBlank(message = "产品编号不能为空！")
	private String code;
	@NotBlank
	private String name;//产品简称
	@NotBlank
	private String fullName;//产品全称
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
	@Enumerations(values = { "MANUALINPUT", "FIRSTRACKTIME" }, message = "募集开始时间类型参数错误")
	@NotBlank
	private String raiseStartDateType;//募集开始时间类型
	@DateTimeFormat(pattern = "YYYY-MM-DD HH:MM:SS")
	private String raiseStatrtDate;//募集开始时间
	@Range(min = 1, max = 240, message = "募集期参数错误")
	@Digits(integer = 4, fraction = 0, message = "募集期参数错误")
	private int raisePeriod;//募集期:()个自然日
	@Range(min = 1, max = 240, message = "起息日参数错误")
	@Digits(integer = 4, fraction = 0, message = "起息日参数错误")
	private int interestsFirstDate;//起息日:募集满额后()个自然日
	@Range(min = 1, max = 240, message = "存续期参数错误")
	@Digits(integer = 4, fraction = 0, message = "存续期参数错误")
	private int durationPeriod;//存续期:()个自然日
	@Digits(integer = 4, fraction = 2, message = "预期年化收益参数错误")
	@NotBlank(message = "预期年化收益不可为空")
	private String expAror;//预期年化收益率
	@Digits(integer = 4, fraction = 2, message = "预期年化收益参数错误")
	private String expArorSec;//预期年化收益率区间
	@Digits(integer = 12, fraction = 0, message = "募集总份额参数错误")
	@Min(value = 1, message = "募集总份额参数错误")
//	@NotBlank(message = "募集总份额不可为空")
	private int raisedTotalNumber;//募集总份额
	@Digits(integer = 12, fraction = 4, message = "单位份额净值参数错误")
	@NotBlank(message = "单位份额净值不可为空")
	private String netUnitShare;//单位份额净值
	@Digits(integer = 12, fraction = 0, message = "单笔投资最低份额参数错误")
	@Min(value = 1, message = "单笔投资最低份额参数错误")
	private int investMin;//单笔投资最低份额
	@Digits(integer = 12, fraction = 0, message = "单笔投资最高份额参数错误")
	private int investMax;//单笔投资最高份额
	@Digits(integer = 12, fraction = 0, message = "申购上限参数错误")
	private int purchaseLimit;//申购上限
	@Digits(integer = 12, fraction = 0, message = "单笔投资追加份额参数错误")
	private int investAdditional;//单笔投资追加份额
	@Range(min = 1, max = 240, message = "还本付息日参数错误")
	@Digits(integer = 4, fraction = 0, message = "还本付息日参数错误")
	private int accrualDate;//还本付息日 存续期结束后第()个自然日
	private String investComment;//投资标的
	@Length(max = 10000, message = "产品说明长度不能超过10000（包含）！")
	@NotBlank
	private String instruction;//产品说明
	@Enumerations(values = { "R1", "R2", "R3", "R4", "R5" }, message = "风险等级参数错误")
	private String riskLevel;//风险等级
	@Valid
	private List<SaveFileForm> files;//附加文件
	
	
}
