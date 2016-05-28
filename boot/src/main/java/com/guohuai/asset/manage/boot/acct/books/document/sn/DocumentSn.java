package com.guohuai.asset.manage.boot.acct.books.document.sn;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOCUMENT_SN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSn implements Serializable {

	private static final long serialVersionUID = -4202296175183023546L;

	@Id
	private String oid;
	private int sn;

}
