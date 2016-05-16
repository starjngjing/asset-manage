package com.guohuai.asset.manage.boot.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.operate.api.AdminAmpSdk;
import com.guohuai.operate.api.objs.admin.amp.AdminAmpObj;
import com.guohuai.asset.manage.component.exception.AMPException;

@Service
public class AdminService {

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private AdminAmpSdk adminAmpSdk;

	@Transactional
	public Admin get(String oid) {
		Admin c = this.adminDao.findOne(oid);
		if (null == c) {
			AdminAmpObj a = this.adminAmpSdk.getAdmin(oid);
			if (a.getErrorCode() != 0) {
				throw new AMPException(a.getErrorMessage());
			}
			c = new Admin();
			c.setOid(oid);
			c.setPublishCount(0);
			c.setMatchingCount(0);
			c.setSetupCount(0);
			c.setOperator(oid);
			c.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			c.setCreateTime(new Timestamp(System.currentTimeMillis()));
			this.adminDao.save(c);
		}
		return c;
	}

	@Transactional
	public List<Admin> get(List<String> oids) {
		List<Admin> admins = adminDao.findAll(oids);

		if (null == admins) {
			admins = new ArrayList<Admin>();
		}

		if (admins.size() > 0) {
			for (Admin admin : admins) {
				oids.remove(admin.getOid());
			}
		}

		if (oids.size() > 0) {
			for (String oid : oids) {
				Admin admin = this.get(oid);
				if (null != admin) {
					admins.add(admin);
				}
			}
		}

		return admins;
	}

	@Transactional
	public List<Admin> list() {
		List<Admin> result = this.adminDao.findAll();
		return result;
	}

	@Transactional
	public List<Admin> list(String... oids) {
		if (null == oids || oids.length == 0) {
			return new ArrayList<Admin>();
		}
		List<String> list = Arrays.asList(oids);
		List<Admin> result = this.adminDao.findAll(list);
		return result;
	}
}
