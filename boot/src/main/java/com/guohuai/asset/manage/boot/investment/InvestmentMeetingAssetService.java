package com.guohuai.asset.manage.boot.investment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
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

	@Autowired
	private InvestmentMeetingService investmentMeetingService;

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

	/**
	 * 根据会议获得标的列表
	 * 
	 * @param meeting
	 * @return
	 */
	public List<Investment> getInvestmentByMeeting(String meetingOid) {
		InvestmentMeeting meeting = investmentMeetingService.getMeetingDet(meetingOid);
		List<InvestmentMeetingAsset> lists = investmentMeetingAssetDao.findByInvestmentMeeting(meeting);
		List<Investment> investments = new ArrayList<Investment>();
		for (InvestmentMeetingAsset temp : lists) {
			Investment entity = new Investment();
			BeanUtils.copyProperties(temp.getInvestment(), entity);
			investments.add(entity);
		}
		return investments;
	}

}
