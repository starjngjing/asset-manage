package com.guohuai.asset.manage.boot.acct.books.document.entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentEntryService {

	@Autowired
	private DocumentEntryDao documentEntryDao;

	@Transactional
	public DocumentEntry save(DocumentEntry entry) {
		entry = this.documentEntryDao.save(entry);
		return entry;
	}

}
