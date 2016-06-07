package com.guohuai.asset.manage.boot.system.config.risk.warning;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RiskWarningService {
	@Autowired
	private RiskWarningDao riskWarningDao;
}
