package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.component.util.DateUtil;

@Service
@Transactional
public class InvestmentMeetingService {

	@Autowired
	private InvestmentMeetingDao investmentMeetingDao;

	/**
	 * 获得投资标的过会列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeeting> getMeetingList(Specification<InvestmentMeeting> spec,
			Pageable pageable) {
		return investmentMeetingDao.findAll(spec, pageable);
	}

	/**
	 * 获得投资标的过会详情
	 * 
	 * @param oid
	 * @return
	 */
	public InvestmentMeeting getMeetingDet(String oid) {
		return investmentMeetingDao.findOne(oid);
	}

	/**
	 * 新建投资标的
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeeting saveInvestment(InvestmentMeeting entity, String operator) {
		entity.setCreateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentMeetingDao.save(entity);
	}

	/**
	 * 修改投资标的
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeeting updateInvestment(InvestmentMeeting entity, String operator) {
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentMeetingDao.save(entity);
	}
}
