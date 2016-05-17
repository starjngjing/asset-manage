package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvestmentMeetingCheckService {

	@Autowired
	private InvestmentMeetingCheckDao investmentMeetingCheckDao;

	/**
	 * 获得投资标的过会检查项列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeetingCheck> getMeetingCheckList(Specification<InvestmentMeetingCheck> spec,
			Pageable pageable) {
		return investmentMeetingCheckDao.findAll(spec, pageable);
	}

	/**
	 * 获得投资标的过会检查项详情
	 * 
	 * @param oid
	 * @return
	 */
	public InvestmentMeetingCheck getMeetingChecktDet(String oid) {
		return investmentMeetingCheckDao.findOne(oid);
	}

	/**
	 * 新建或更新投资标的过会检查项
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeetingCheck saveOrUpdateMeetingCheck(InvestmentMeetingCheck entity, String operator) {
		return this.investmentMeetingCheckDao.save(entity);
	}
	

}
