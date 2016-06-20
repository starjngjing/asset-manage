package com.guohuai.asset.manage.boot.system.config.project.warrantyLevel;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class CCPWarrantyLevelService {

	@Autowired
	private CCPWarrantyLevelDao CCPWarrantyLevelDao;

	@Transactional
	public CCPWarrantyLevel save(CCPWarrantyLevelForm form) {
		CCPWarrantyLevel level = new CCPWarrantyLevel();
		BeanUtils.copyProperties(form, level);
		CCPWarrantyLevel warrantyExpire = this.CCPWarrantyLevelDao.save(level);

		return warrantyExpire;

	}

	@Transactional
	public void delete(String oid) {

		this.CCPWarrantyLevelDao.delete(oid);

	}

	public CCPWarrantyLevel get(String oid) {

		CCPWarrantyLevel warrantyLevel = this.CCPWarrantyLevelDao.findOne(oid);

		if (null == warrantyLevel) {
			throw new AMPException(String.format("No data found for oid '%s'", oid));
		}

		return warrantyLevel;

	}

	public List<CCPWarrantyLevel> search() {
		List<CCPWarrantyLevel> list = this.CCPWarrantyLevelDao.search();
		return list;
	}

}
