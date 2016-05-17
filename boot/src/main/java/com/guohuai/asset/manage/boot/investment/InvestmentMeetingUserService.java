package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvestmentMeetingUserService {

	@Autowired
	private InvestmentMeetingUserDao investmentMeetingUserDao;

	/**
	 * 获得投资标的过会人员列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeetingUser> getMeetingUserList(Specification<InvestmentMeetingUser> spec,
			Pageable pageable) {
		return investmentMeetingUserDao.findAll(spec, pageable);
	}

	/**
	 * 获得投资标的过会人员详情
	 * 
	 * @param oid
	 * @return
	 */
	public InvestmentMeetingUser getMeetingUserDet(String oid) {
		return investmentMeetingUserDao.findOne(oid);
	}

	/**
	 * 新建投资标的过会人员
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeetingUser saveOrUpdateInvestment(InvestmentMeetingUser entity, String operator) {
		return this.investmentMeetingUserDao.save(entity);
	}

}
