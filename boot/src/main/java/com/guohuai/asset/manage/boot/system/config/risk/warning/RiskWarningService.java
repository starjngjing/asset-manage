package com.guohuai.asset.manage.boot.system.config.risk.warning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.boot.system.config.risk.warning.options.RiskWarningOptions;
import com.guohuai.asset.manage.component.exception.AMPException;

@Service
public class RiskWarningService {
	@Autowired
	private RiskWarningDao riskWarningDao;
	
	@Transactional
	public RiskWarning save(RiskWarningForm form) {
		return null;
	}
	
	@Transactional
	public RiskWarning save(RiskWarning entity) {
		return riskWarningDao.save(entity);
	}
	

	@Transactional
	public void delete(String oid) {
		this.riskWarningDao.delete(oid);
	}


	@Transactional
	public RiskWarning get(String oid) {
		RiskWarning indicate = this.riskWarningDao.findOne(oid);

		if (null == indicate) {
			throw new AMPException(String.format("No data found for oid '%s'", oid));
		}

		return indicate;
	}

	@Transactional
	public List<RiskWarningResp> search(String type, String keyword) {

		List<RiskWarning> list = this.riskWarningDao.search(String.format("%%%s%%", type), String.format("%%%s%%", keyword), new String[] { RiskIndicate.STATE_Enable, RiskIndicate.STATE_Disable });

		List<RiskWarningResp> result = new ArrayList<RiskWarningResp>();
		for (RiskWarning i : list) {
			result.add(new RiskWarningResp(i));
		}

		return result;
	}

}
