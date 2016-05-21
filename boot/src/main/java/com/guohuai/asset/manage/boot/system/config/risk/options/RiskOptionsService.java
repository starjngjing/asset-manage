package com.guohuai.asset.manage.boot.system.config.risk.options;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateService;

@Service
public class RiskOptionsService {

	@Autowired
	private RiskOptionsDao riskOptionsDao;
	@Autowired
	private RiskIndicateService riskIndicateService;

	@Transactional
	public RiskOptionsResp save(RiskOptionsForm form) {
		return null;
	}
	
}
