package com.guohuai.asset.manage.boot.system.config.risk.indicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.cate.RiskCate;
import com.guohuai.asset.manage.boot.system.config.risk.cate.RiskCateService;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class RiskIndicateService {

	@Autowired
	private RiskIndicateDao riskIndicateDao;
	@Autowired
	private RiskCateService riskCateService;

	@Transactional
	public RiskIndicate save(RiskIndicateForm form) {

		RiskCate cate = null;
		if (StringUtil.isEmpty(form.getCateOid())) {
			cate = this.riskCateService.save(form.getCateType(), form.getCateTitle());
		} else {
			cate = this.riskCateService.get(form.getCateOid());
		}

		RiskIndicate indicate;

		if (StringUtil.isEmpty(form.getIndicateOid())) {
			indicate = new RiskIndicate();
			indicate.setOid(StringUtil.uuid());
			indicate.setState(RiskIndicate.STATE_Enable);
		} else {
			indicate = this.get(form.getIndicateOid());
		}

		indicate.setCate(cate);
		indicate.setTitle(form.getIndicateTitle());
		indicate.setDataType(form.getIndicateDataType());
		indicate.setDataUnit(form.getIndicateDataUnit());

		indicate = this.riskIndicateDao.save(indicate);

		return indicate;
	}

	@Transactional
	public RiskIndicate delete(String oid) {
		RiskIndicate i = this.get(oid);
		i.setState(RiskIndicate.STATE_Delete);
		i = this.riskIndicateDao.save(i);
		return i;
	}

	@Transactional
	public RiskIndicate enable(String oid) {
		RiskIndicate i = this.get(oid);
		i.setState(RiskIndicate.STATE_Enable);
		i = this.riskIndicateDao.save(i);
		return i;
	}

	@Transactional
	public RiskIndicate disable(String oid) {
		RiskIndicate i = this.get(oid);
		i.setState(RiskIndicate.STATE_Disable);
		i = this.riskIndicateDao.save(i);
		return i;
	}

	@Transactional
	public RiskIndicate get(String oid) {
		RiskIndicate indicate = this.riskIndicateDao.findOne(oid);

		if (null == indicate) {
			throw new AMPException(String.format("No data found for oid '%s'", oid));
		}

		return indicate;
	}

	@Transactional
	public List<RiskIndicateResp> search(String type, String keyword) {

		List<RiskIndicate> list = this.riskIndicateDao.search(String.format("%%%s%%", type), String.format("%%%s%%", keyword), new String[] { RiskIndicate.STATE_Enable, RiskIndicate.STATE_Disable });

		List<RiskIndicateResp> result = new ArrayList<RiskIndicateResp>();
		for (RiskIndicate i : list) {
			result.add(new RiskIndicateResp(i));
		}

		return result;
	}

	@Transactional
	public List<RiskIndicateOption> options(String type) {

		List<RiskIndicate> list = this.riskIndicateDao.search(type, new String[] { RiskIndicate.STATE_Enable });

		List<RiskIndicateOption> options = new ArrayList<RiskIndicateOption>();
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

}
