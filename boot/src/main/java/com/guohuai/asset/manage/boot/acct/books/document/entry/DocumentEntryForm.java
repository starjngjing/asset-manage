package com.guohuai.asset.manage.boot.acct.books.document.entry;

import java.sql.Date;

import lombok.Data;

@Data
public class DocumentEntryForm {

	private Date startDate;
	private Date endDate;
	private int page;
	private int size;

}
