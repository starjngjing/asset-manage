package com.guohuai.asset.manage.boot.product;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import com.guohuai.asset.manage.boot.dict.Dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

/**
 * 产品实体
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_PRODUCT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {
	
	private static final long serialVersionUID = 7767000944338560987L;
	
	public static final String YES = "YES";
	public static final String NO = "NO";
	
	/**
	 * 	状态status：
	 */
	public static final String STATE_Create = "CREATE";//新建
	public static final String STATE_Update = "UPDATE";//提交修改
	public static final String STATE_Notstartraise = "NOTSTARTRAISE";//未开始募集
	public static final String STATE_Raising = "RAISING";//募集中
	public static final String STATE_Raiseend = "RAISEEND";//募集结束
	public static final String STATE_Risefail = "RISEFAIL";//募集失败
	public static final String STATE_Subsist = "SUBSIST";//存续期
	public static final String STATE_Subsistend = "SUBSISTEND";//存续期结束(还本付息期)
	public static final String STATE_Liquidation = "LIQUIDATION";//已清算
	/**
	 * 	状态status：
	 *  waitPutOn:未排期
	 *  putOnShelf:已排期
	 *  collecting:募集期
	 *  putOffShelf:下架
	 *  collectingUnreached:募集未满
	 *  running:运行期
	 *  abortionWaitCalc:流标结算中
	 *  endWaitCacl:到期结算中
	 *  abortionEnd:流标终止
	 *  end:到期完成
	 */
	public static final String PRODUCT_STATUS_waitPutOn = "waitPutOn";
	public static final String PRODUCT_STATUS_putOnShelf = "putOnShelf";
	public static final String PRODUCT_STATUS_collecting = "collecting";
	public static final String PRODUCT_STATUS_putOffShelf = "putOffShelf";
	public static final String PRODUCT_STATUS_collectingUnreached = "collectingUnreached";
	public static final String PRODUCT_STATUS_running = "running";
	public static final String PRODUCT_STATUS_abortionWaitCalc = "abortionWaitCalc";
	public static final String PRODUCT_STATUS_endWaitCalc = "endWaitCalc";
	public static final String PRODUCT_STATUS_abortionEnd = "abortionEnd";
	public static final String PRODUCT_STATUS_end = "end";
	
	
	
	

	public static final String RETURN_CYCLE_Month = "MONTH";//按月
	public static final String RETURN_CYCLE_Day = "DAY";//按日
	
	public static final String DATE_TYPE_Natrue = "NATRUE";//自然日
	public static final String DATE_TYPE_Trade = "TRADE";//交易日
	
	public static final String RISK_LEVEL_Low = "LOW";//低
	public static final String RISK_LEVEL_Mid = "MID";//
	public static final String RISK_LEVEL_High = "HIGH";//低
	
	public static final String AUDIT_STATE_Nocommit = "NOCOMMIT";//未提交审核
	public static final String AUDIT_STATE_Auditing = "AUDITING";//待审核(已经提交:审核中)
	public static final String AUDIT_STATE_Reviewing = "REVIEWING";//待复核(已经提交复核:复核中)
	public static final String AUDIT_STATE_Approvaling = "APPROVALING";//待批准(已经提交批准申请:批准申请中)
	public static final String AUDIT_STATE_Approval = "APPROVAL";//批准
	public static final String AUDIT_STATE_Reject = "REJECT";//驳回
	
	public static final int OPEN_Off = 0;
	public static final int OPEN_On = 1;
	
	public static final String STEMS_Userdefine = "USERDEFINE";//前端添加
	public static final String STEMS_Plateform = "PLATEFORM";//后台添加
	
	public static final String DATE_TYPE_ManualInput  = "MANUALINPUT";//固定时间(手动录入时间)
	public static final String DATE_TYPE_FirstRackTime  = "FIRSTRACKTIME";//;与首次上架时间同时
	
	public static final String UNIT_Month = "MONTH";
	public static final String UNIT_Year = "YEAR";
	public static final String UNIT_Day = "DAY";
	
	@Id
	private String oid;//产品序号
	private String code;//产品编号
	private String name;//产品名称
	private String fullName;//产品全称
	private String manager;//产品管理人简称
	private String administrator;//产品管理人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeOid", referencedColumnName = "oid")
	private Dict type;//产品类型
	private String reveal;//额外增信
	private String currency;//币种
	private String incomeCalcBasis;//收益计算基础
	private BigDecimal manageRate;//托管费率
	private BigDecimal fixedManageRate;//固定管理费率
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accrualCycleOid", referencedColumnName = "oid")
	private Dict accrualCycle;//收益结转周期
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payModeOid", referencedColumnName = "oid")
	private Dict payMode;//付利方式
	private int payModeDate;//付利具体几号
	private String raiseStartDateType;//募集开始时间类型
	private Timestamp raiseStartDate;//募集开始时间
	private int raisePeriod;//募集期:()个自然日
	private int lockPeriod;//锁定期:()个自然日 一旦申购，将冻结此金额T+5天。
	private int interestsFirstDate;//起息日:募集满额后()个自然日
	private int durationPeriod;//存续期:()个自然日
	private BigDecimal expAror;//预期年化收益率
	private BigDecimal expArorSec;//预期年化收益率区间
	private int raisedTotalNumber;//募集总份额
	private BigDecimal netUnitShare;//单位份额净值
	private int investMin;//单笔投资最低份额
	private int investMax;//单笔投资最高份额
	private int investAdditional;//单笔投资追加份额
	private int accrualDate;//还本付息日 存续期结束后第()个自然日
	private int netMaxRredeemDay;//单日净赎回上限
	private int purchaseConfirmDate;//申购确认日:()个
	private String purchaseConfirmDateType;//申购确认日类型:自然日或交易日
	private int redeemConfirmDate;//赎回确认日:()个
	private String redeemConfirmDateType;//赎回确认日类型:自然日或交易日
	private String setupDateType;//产品成立时间类型
	private Timestamp setupDate;//产品成立时间（存续期开始时间）
	private int redeemTimingTaskDate;//赎回定时任务天数	 默认每日
	private String redeemTimingTaskDateType;//赎回定时任务类型:自然日或交易日
	private Time redeemTimingTaskTime;//赎回定时任务时间 填写每日定时调支付接口做批量赎回操作的时间点
	private String investComment;//投资标的
	private String instruction;//产品说明
	private String riskLevel;//风险等级
	private String fileKeys;//附加文件
	private String status;//产品状态
	
	private Timestamp createTime;//创建时间
	private Timestamp updateTime;//更新时间
	private String operator;//操作员
	private String publisher;//发行人
	private int currentVolume;//当前份额
	private int collectedVolume;//已募份额
	private int purchaseNum;//已投次数
	private int lockCollectedVolume;//锁定已募份额
	
	private Timestamp raiseEndDate;//募集结束时间
	private Timestamp raiseFailDate;//募集宣告失败时间
	private Timestamp durationPeriodEndDate;//存续期结束时间
	private Timestamp accrualLastDate;//到期还款时间
	private String isOpenPurchase;//是否开放申购期
	private String isOpenRemeed;//是否开放赎回期
	private Date endDate;//产品清算（结束）时间
	private Timestamp durationRepaymentTime;//存续期内还款时间
	private String stems;//来源
	private String isDeleted;
	
	private String state;//产品状态
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetPoolOid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;//资产配置
	private Timestamp endTime;//产品清算（结束）时间
	private String auditState;//审核状态

}
