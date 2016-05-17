package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvestmentMeetingVoteService {

	@Autowired
	private InvestmentMeetingVoteDao investmentMeetingVoteDao;

	/**
	 * 获得投资标的过会表决列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeetingVote> getMeetingVoteList(Specification<InvestmentMeetingVote> spec,
			Pageable pageable) {
		return investmentMeetingVoteDao.findAll(spec, pageable);
	}

	/**
	 * 获得投资标的过会表决详情
	 * 
	 * @param oid
	 * @return
	 */
	public InvestmentMeetingVote getMeetingVoteDet(String oid) {
		return investmentMeetingVoteDao.findOne(oid);
	}

	/**
	 * 新建或更新投资标的过会表决
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	public InvestmentMeetingVote saveOrUpdateInvestment(InvestmentMeetingVote entity, String operator) {
		return this.investmentMeetingVoteDao.save(entity);
	}
}
