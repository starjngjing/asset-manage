package com.guohuai.asset.manage.boot.acct.doc.word;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_ACCT_DOCWORD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocWord implements Serializable {

	private static final long serialVersionUID = -5211577225945179723L;

	public static final String DFT_Yes = "YES";
	public static final String DFT_No = "NO";

	@Id
	private String oid;
	private String dicword;
	private String title;
	private String dft;

}
