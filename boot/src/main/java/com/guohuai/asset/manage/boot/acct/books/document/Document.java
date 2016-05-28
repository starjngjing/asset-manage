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

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "docwordOid", referencedColumnName = "oid")
	private DocWord docword;
	private String type;
	private String relative;
	private String ticket;
	private int acctSn;
	private Date acctDatee;
	private int invoiceNum;
	private BigDecimal drAmount;
	private BigDecimal crAmount;
	private Timestamp createTime;
	private Timestamp updateTime;

}
