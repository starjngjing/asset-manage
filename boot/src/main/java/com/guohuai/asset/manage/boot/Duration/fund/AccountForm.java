package com.guohuai.asset.manage.boot.Duration.fund;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.Data;

/**
 * 存续期（出入金明细）--账户对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
public class AccountForm {

	@Id
	private String id;
	private Date currDate;
	// 操作类型（申购，赎回，转让）
	private String operation;
	// 金额
	private BigDecimal capital;
	// 投资的标的名
	private String target;
	// 
	private String type;
	// 状态（未审核，资金处理中，完成）
	private String status;
}
