package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvestmentMeetingAssetService {

	@Autowired
	private InvestmentMeetingAssetDao investmentMeetingAssetDao;

	/**
	 * 获得过会投资标的列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeetingAsset> getMeetingAssetList(Specification<InvestmentMeetingAsset> spec,
			Pageable pageable) {
		return investmentMeetingAssetDao.findAll(spec, pageable);
	}

	/**
	 * 获得过会投资标的详情
	 * 
	 * @param oid
	 * @return
	 */
	public InvestmentMeetingAsset getMeetingAssetDet(String oid) {
		return investmentMeetingAssetDao.findOne(oid);
	}

	/**
	 * 新建或更新过会投资标的
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeetingAsset saveOrUpdateMeetingAsset(InvestmentMeetingAsset entity, String operator) {

		return this.investmentMeetingAssetDao.save(entity);
	}

}
