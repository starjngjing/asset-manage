package com.guohuai.asset.manage.boot.acct.books.document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.acct.doc.word.DocWord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOCUMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document implements Serializable {

	private static final long serialVersionUID = 4672944310612688105L;

	// 现金管理工具 调仓
	public static final String TYPE_CASHTOOL_WAREHOUSE = "CASHTOOL_WAREHOUSE";
	// 信托计划 申购
	public static final String TYPE_TARGET_PURCHASE = "TARGET_PURCHASE";
	// 信托计划 转让
	public static final String TYPE_TARGET_TRANSFER = "TARGET_TRANSFER";
	// 信托计划 本息兑付
	public static final String TYPE_TARGET_INCOME = "TARGET_INCOME";

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "docwordOid", referencedColumnName = "oid")
	private DocWord docword;
	private String type;
	private String relative;
	private String ticket;
	private int acctSn;
	private Date acctDate;
	private int invoiceNum;
	private BigDecimal drAmount;
	private BigDecimal crAmount;
	private Timestamp createTime;
	private Timestamp updateTime;

}
