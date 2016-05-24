package com.guohuai.asset.manage.boot.investment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.investment.manage.InvestmentCheckDetResp;

@Service
@Transactional
public class InvestmentMeetingCheckService {

	@Autowired
	private InvestmentMeetingCheckDao investmentMeetingCheckDao;

	@Autowired
	private InvestmentService investmentService;

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

	/**
	 * 根据标的ID获得检查项
	 * 
	 * @param investmentOid
	 * @return
	 */
	public List<InvestmentCheckDetResp> getMeetingCheckListByInvestmentOid(String investmentOid) {
		List<InvestmentCheckDetResp> res = new ArrayList<InvestmentCheckDetResp>();
		Investment investment = investmentService.getInvestment(investmentOid);
		if (null == investment) {
			throw new RuntimeException();
		}
		List<InvestmentMeetingCheck> lists = investmentMeetingCheckDao.findByInvestment(investment);
		for (InvestmentMeetingCheck entity : lists) {
			res.add(new InvestmentCheckDetResp(entity));
		}
		return res;
	}

}
