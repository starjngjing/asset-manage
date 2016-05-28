package com.guohuai.asset.manage.boot.acct.books.document.entry;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.acct.books.document.Document;
import com.guohuai.asset.manage.boot.acct.books.document.DocumentService;

@Service
public class DocumentEntryService {

	@Autowired
	private DocumentEntryDao documentEntryDao;
	@Autowired
	private DocumentService documentService;

	@Transactional
	public DocumentEntry save(DocumentEntry entry) {
		entry = this.documentEntryDao.save(entry);
		return entry;
	}

	@Transactional
	public DocumentEntryPage search(Date startDate, Date endDate, int page, int size) {
		Page<Document> documents = this.documentService.search(startDate, endDate, page, size);

		DocumentEntryPage ep = new DocumentEntryPage();

		if (null != documents.getContent() && documents.getContent().size() > 0) {
			ep.setTotal(documents.getTotalElements());
			ep.setNumber(documents.getContent().size());

			List<DocumentEntry> entries = this.documentEntryDao.search(documents.getContent());
			LinkedHashMap<String, List<DocumentEntry>> maps = new LinkedHashMap<String, List<DocumentEntry>>();

			for (DocumentEntry e : entries) {
				if (!maps.containsKey(e.getDocument().getOid())) {
					maps.put(e.getDocument().getOid(), new ArrayList<DocumentEntry>());
				}
				maps.get(e.getDocument().getOid()).add(e);
			}

			int index = 0;
			for (String key : maps.keySet()) {
				for (int i = 0; i < maps.get(key).size(); i++) {
					DocumentEntryResp r = new DocumentEntryResp(maps.get(key).get(i));
					if (i == 0) {
						r.setMaster(true);
						r.setRowspan(maps.get(key).size());
					}
					r.setIndex(index);
					ep.getRows().add(r);
				}
				index++;
			}

		}

		return ep;
	}

}
