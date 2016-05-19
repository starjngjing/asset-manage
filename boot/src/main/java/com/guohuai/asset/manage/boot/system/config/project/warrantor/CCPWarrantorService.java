package com.guohuai.asset.manage.boot.system.config.project.warrantor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class CCPWarrantorService {

	@Autowired
	private CCPWarrantorDao ccpWarrantorDao;

	@Transactional
	public CCPWarrantor create(CCPWarrantorForm form) {

		CCPWarrantor.CCPWarrantorBuilder builder = CCPWarrantor.builder().oid(StringUtil.uuid());
		builder.title(form.getTitle()).lowScore(form.getLowScore()).highScore(form.getHighScore()).weight(form.getWeight100() / 100);

		CCPWarrantor warrantor = this.ccpWarrantorDao.save(builder.build());

		return warrantor;

	}

	@Transactional
	public CCPWarrantor update(CCPWarrantorForm form) {

		CCPWarrantor warrantor = this.get(form.getOid());

		warrantor.setTitle(form.getTitle());
		warrantor.setLowScore(form.getLowScore());
		warrantor.setHighScore(form.getHighScore());
		warrantor.setWeight(form.getWeight100() / 100);

		warrantor = this.ccpWarrantorDao.save(warrantor);

		return warrantor;

	}

	@Transactional
	public void delete(String oid) {

		this.ccpWarrantorDao.delete(oid);

	}

	public CCPWarrantor get(String oid) {

		CCPWarrantor warrantor = this.ccpWarrantorDao.findOne(oid);

		if (null == warrantor) {
			throw new AMPException(String.format("No data found for oid '%s'", oid));
		}

		return warrantor;

	}

	public List<CCPWarrantor> search() {
		List<CCPWarrantor> list = this.ccpWarrantorDao.search();
		return list;
	}

}
