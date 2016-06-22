package com.guohuai.asset.manage.boot.duration.fact.feigin;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;

@Service
public class FeiginService {

	@Autowired
	private FeiginDao feiginDao;
	
	@Transactional
	public void save(FeiginEntity entity) {
		feiginDao.save(entity);
	}
	
	@Transactional
	public FeiginEntity getByOid(String oid) {
		return feiginDao.findOne(oid);
	}
	
	@Transactional
	public void create(FeiginForm form, String operator) {
		FeiginEntity entity = new FeiginEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAssetPoolOid(form.getAssetPoolOid());
		entity.setChargeFee(form.getChargeFee());
		entity.setDigest(form.getDigest());
		entity.setState("0");
		entity.setCreator(operator);
		entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		feiginDao.save(entity);
	}
	
	@Transactional
	public void updateByOid(String oid, String operator) {
		FeiginEntity entity = feiginDao.findOne(oid);
		entity.setState("1");
		entity.setDrawer(operator);
		entity.setDrawTime(new Timestamp(System.currentTimeMillis()));
		feiginDao.save(entity);
	}
	
	@Transactional
	public void deleteByOid(String oid, String operator) {
		FeiginEntity entity = feiginDao.findOne(oid);
		entity.setState("-1");
		entity.setDrawer(operator);
		entity.setDrawTime(new Timestamp(System.currentTimeMillis()));
		feiginDao.save(entity);
	}
	
	@Transactional
	public FeiginForm getFormByOid(Specification<FeiginEntity> spec) {
		FeiginEntity entity = feiginDao.findOne(spec);
		FeiginForm form = FeiginForm.builder()
				.oid(entity.getOid())
				.assetPoolOid(entity.getAssetPoolOid())
				.chargeFee(entity.getChargeFee())
				.digest(entity.getDigest())
				.state(entity.getState())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.build();
		return form;
	}
	
	@Transactional
	public PageResp<FeiginForm> getAll(Specification<FeiginEntity> spec, Pageable pageable) {
		List<FeiginForm> formList = Lists.newArrayList();
		Page<FeiginEntity> list = feiginDao.findAll(spec, pageable);
		if (null != list && !list.getContent().isEmpty()) {
			for (FeiginEntity entity : list.getContent()) {
				FeiginForm form = FeiginForm.builder()
					.oid(entity.getOid())
					.assetPoolOid(entity.getAssetPoolOid())
					.chargeFee(entity.getChargeFee())
					.digest(entity.getDigest())
					.state(entity.getState())
					.creator(entity.getCreator())
					.createTime(entity.getCreateTime())
					.build();
				formList.add(form);
			}
		}
		PageResp<FeiginForm> rep = new PageResp<FeiginForm>();
		rep.setRows(formList);
		rep.setTotal(list.getTotalElements());
		return rep;
	}
}
