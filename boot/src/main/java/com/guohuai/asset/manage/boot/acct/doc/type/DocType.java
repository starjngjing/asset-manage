package com.guohuai.asset.manage.boot.acct.doc.type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOC_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocType implements Serializable {

	private static final long serialVersionUID = 9101470308333109896L;

	@Id
	private String oid;
	private String name;

}
