package com.guohuai.asset.manage.boot.acct.books.document.sn;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentSnService {

	@Autowired
	private DocumentSnDao documentSnDao;

	private DateFormat format = new SimpleDateFormat("yyyyMM");

	@Transactional
	public int genSn(Date date) {
		String oid = this.format.format(date);
		DocumentSn sn = this.documentSnDao.findOne(oid);
		if (null == sn) {
			sn = new DocumentSn();
			sn.setOid(oid);
			sn.setSn(1);
			sn = this.documentSnDao.save(sn);
			return sn.getSn();
		} else {
			sn.setSn(sn.getSn() + 1);
			sn = this.documentSnDao.save(sn);
			return sn.getSn();
		}
	}

}
