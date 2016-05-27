package com.guohuai.asset.manage.boot.acct.doc.template.entry;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.acct.account.Account;
import com.guohuai.asset.manage.boot.acct.doc.template.DocTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOC_TEMPLATE_ENTRY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocTemplateEntry implements Serializable {

	private static final long serialVersionUID = -8032688933147717474L;

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "templateOid", referencedColumnName = "oid")
	private DocTemplate template;
	private String direction;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountOid", referencedColumnName = "oid")
	private Account account;
	private String digest;
	private int seq;

}
