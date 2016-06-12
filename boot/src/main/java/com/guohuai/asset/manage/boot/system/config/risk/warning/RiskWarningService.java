package com.guohuai.asset.manage.boot.system.config.risk.warning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateOption;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateService;
import com.guohuai.asset.manage.boot.system.config.risk.warning.options.RiskWarningOptions;
import com.guohuai.asset.manage.component.exception.AMPException;

@Service
public class RiskWarningService {
	@Autowired
	private RiskWarningDao riskWarningDao;
	@Autowired
	private RiskIndicateService  riskIndicateService;
	
	@Transactional
	public RiskWarning save(RiskWarningForm form) {
		RiskWarning warning = new RiskWarning();
		warning.setOid(form.getOid());
		warning.setIndicate(riskIndicateService.get(form.getIndicateOid()));
		warning.setTitle(form.getTitle());
		return riskWarningDao.save(warning);
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

/*
	@Transactional
	public List<RiskWarningResp> options(String type) {

		List<RiskWarning> list = this.riskWarningDao.search(type, new String[] { RiskIndicate.STATE_Enable });

		List<RiskWarningResp> options = new ArrayList<RiskIndicateOption>();
		Map<String, RiskIndicateOption> map = new HashMap<String, RiskIndicateOption>();

		if (null != list && list.size() > 0) {
			for (RiskIndicate i : list) {
				if (!map.containsKey(i.getCate().getOid())) {
					RiskIndicateOption rio = new RiskIndicateOption();
					rio.setOid(i.getCate().getOid());
					rio.setTitle(i.getCate().getTitle());
					options.add(rio);
					map.put(i.getCate().getOid(), rio);
				}

				RiskIndicateOption rio = map.get(i.getCate().getOid());

				RiskIndicateOption.Option roo = new RiskIndicateOption.Option();
				roo.setOid(i.getOid());
				roo.setTitle(i.getTitle());
				roo.setDataType(i.getDataType());
				roo.setDataUnit(i.getDataUnit());

				rio.getOptions().add(roo);

			}
		}

		return options;
	}
	*/
}
