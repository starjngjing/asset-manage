package com.guohuai.asset.manage.boot.acct.books.document.entry;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.acct.books.AccountBook;
import com.guohuai.asset.manage.boot.acct.books.document.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOCUMENT_ENTRY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentEntry implements Serializable {

	private static final long serialVersionUID = -3680010504828506144L;

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "documentOid", referencedColumnName = "oid")
	private Document document;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bookOid", referencedColumnName = "oid")
	private AccountBook book;

	private String relative;
	private String ticket;

	private String digest;
	private BigDecimal drAmount;
	private BigDecimal crAmount;
	private int seq;

}
