package com.guohuai.asset.manage.boot.system.config.risk.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicate;
import com.guohuai.asset.manage.boot.system.config.risk.indicate.RiskIndicateService;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class RiskOptionsService {

	@Autowired
	private RiskOptionsDao riskOptionsDao;
	@Autowired
	private RiskIndicateService riskIndicateService;

	@Transactional
	public List<RiskOptionsResp> save(RiskOptionsForm form) {

		RiskIndicate indicate = this.riskIndicateService.get(form.getIndicateOid());
		this.riskOptionsDao.deleteByIndicate(indicate);

		int seq = 0;

		List<RiskOptionsResp> list = new ArrayList<RiskOptionsResp>();

		if (null != form.getOptions() && form.getOptions().size() > 0) {
			for (RiskOptionsForm.Option option : form.getOptions()) {
				RiskOptions.RiskOptionsBuilder builder = RiskOptions.builder().oid(StringUtil.uuid());
				builder.indicate(indicate).score(option.getScore()).dft("NO").seq(seq);
				builder.param0(option.getParam0()).param1(option.getParam1()).param2(option.getParam2()).param3(option.getParam3());
				RiskOptions ro = builder.build();
				ro = this.riskOptionsDao.save(ro);
				list.add(new RiskOptionsResp(ro));
				seq++;
			}
		}

		RiskOptions.RiskOptionsBuilder builder = RiskOptions.builder().oid(StringUtil.uuid());
		builder.indicate(indicate).score(form.getDftScore()).dft("YES").seq(seq);
		RiskOptions dro = builder.build();
		dro = this.riskOptionsDao.save(dro);
		list.add(new RiskOptionsResp(dro));
		return list;
	}

	@Transactional
	public List<RiskOptionsView> showview(String keyword) {

		List<RiskOptionsView> view = new ArrayList<RiskOptionsView>();

		List<RiskOptions> options = this.riskOptionsDao.search(String.format("%%%s%%", keyword));

		if (null != options && options.size() > 0) {
			Map<String, Map<String, List<RiskOptionsView>>> cmap = new HashMap<String, Map<String, List<RiskOptionsView>>>();
			for (RiskOptions o : options) {

				boolean showCate = false;
				if (!cmap.containsKey(o.getIndicate().getCate().getOid())) {
					cmap.put(o.getIndicate().getCate().getOid(), new HashMap<String, List<RiskOptionsView>>());
					showCate = true;
				}

				Map<String, List<RiskOptionsView>> imap = cmap.get(o.getIndicate().getCate().getOid());
				if (!imap.containsKey(o.getIndicate().getOid())) {
					List<RiskOptionsView> nv = new ArrayList<RiskOptionsView>();
					RiskOptionsView tv = new RiskOptionsView();
					tv.setCateOid(o.getIndicate().getCate().getOid());
					tv.setCateTitle(o.getIndicate().getCate().getTitle());
					tv.setShowCate(showCate);
					tv.setIndicateOid(o.getIndicate().getOid());
					tv.setIndicateTitle(o.getIndicate().getTitle());
					tv.setShowIndicate(true);
					tv.setShowOptions(false);
					nv.add(tv);
					imap.put(o.getIndicate().getOid(), nv);
				}

				List<RiskOptionsView> vlist = imap.get(o.getIndicate().getOid());
				RiskOptionsView v = new RiskOptionsView();
				v.setShowCate(false);
				v.setShowIndicate(false);
				v.setOptionsOid(o.getOid());
				v.setOptionsTitle(o.showTitle());
				v.setOptionsScore(o.getScore());
				v.setShowOptions(true);
				vlist.add(v);

			}

			for (String ck : cmap.keySet()) {
				for (String ik : cmap.get(ck).keySet()) {
					view.addAll(cmap.get(ck).get(ik));
				}
			}

		}

		return view;
	}

}
