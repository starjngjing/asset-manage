package com.guohuai.asset.manage.boot.acct.books.document.entry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guohuai.asset.manage.boot.acct.books.document.Document;

public interface DocumentEntryDao extends JpaRepository<DocumentEntry, String> {

	@Query("from DocumentEntry e where e.document in ?1 order by e.document.updateTime desc, e.seq asc")
	public List<DocumentEntry> search(List<Document> documents);

}
