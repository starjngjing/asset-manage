package com.guohuai.asset.manage.boot.system.config.risk.indicate.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.options.RiskOptions;
import com.guohuai.asset.manage.boot.system.config.risk.options.RiskOptionsService;

@Service
public class RiskIndicateCollectService {

	@Autowired
	private RiskIndicateCollectDao riskIndicateCollectDao;
	@Autowired
	private RiskOptionsService riskOptionsService;

	@Transactional
	public List<RiskIndicateCollect> save(RiskIndicateCollectForm form) {

		List<RiskIndicateCollect> result = new ArrayList<RiskIndicateCollect>();

		List<RiskOptions> options = this.riskOptionsService.search(form.getType());
		if (null == options || options.size() == 0) {
			return result;
		}

		Map<String, List<RiskOptions>> omap = new HashMap<String, List<RiskOptions>>();
		for (RiskOptions option : options) {
			if (!omap.containsKey(option.getIndicate().getOid())) {
				omap.put(option.getIndicate().getOid(), new ArrayList<RiskOptions>());
			}
			omap.get(option.getIndicate().getOid()).add(option);
		}

		return null;
	}

}
