package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guohuai.asset.manage.boot.investment.meeting.AddInvestmentMeetingForm;
import com.guohuai.asset.manage.boot.investment.meeting.InvestmentMeetingListResp;
import com.guohuai.asset.manage.component.util.DateUtil;

@Service
@Transactional
public class InvestmentMeetingService {

	@Autowired
	private InvestmentMeetingDao investmentMeetingDao;

	@Autowired
	private InvestmentMeetingAssetDao investmentMeetingAssetDao;

	@Autowired
	private InvestmentMeetingUserDao investmentMeetingUserDao;

	@Autowired
	private InvestmentService investmentService;

	/**
	 * 获得投资标的过会列表
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<InvestmentMeeting> getMeetingList(Specification<InvestmentMeeting> spec, Pageable pageable) {
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
	public InvestmentMeeting saveMeeting(InvestmentMeeting entity, String operator) {
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
	public InvestmentMeeting updateMeeting(InvestmentMeeting entity, String operator) {
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		entity.setOperator(operator);
		return this.investmentMeetingDao.save(entity);
	}
	

	@Transactional
	public InvestmentMeeting addMeeting(AddInvestmentMeetingForm form, String operator) {
		try {
			InvestmentMeeting entity = InvestmentMeeting.builder().conferenceTime(form.getConferenceTime())
					.creator(operator).createTime(DateUtil.getSqlCurrentDate()).sn(form.getSn())
					.state(InvestmentMeeting.MEETING_STATE_NOTOPEN).title(form.getTitle()).build();
			InvestmentMeeting meeting = investmentMeetingDao.save(entity);
			this.addTargetStr(form.getTarget(), meeting, operator);
			this.addParticipant(form.getParticipant(), meeting);
			return meeting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private void addParticipant(String parStr, InvestmentMeeting meeting) throws Exception {
		String[] arr = parStr.split(",");
		for (String oid : arr) {
			InvestmentMeetingUser entity = InvestmentMeetingUser.builder().InvestmentMeeting(meeting)
					.participantOid(oid).build();
			investmentMeetingUserDao.save(entity);
		}
	}

	private void addTargetStr(String targetStr, InvestmentMeeting meeting, String operator) throws Exception {
		String[] arr = targetStr.split(",");
		for (String oid : arr) {
			Investment temp = investmentService.getInvestmentDet(oid);
			InvestmentMeetingAsset entity = InvestmentMeetingAsset.builder().investment(temp).InvestmentMeeting(meeting)
					.build();
			entity.setInvestment(temp);
			entity.setInvestmentMeeting(meeting);
			investmentMeetingAssetDao.save(entity);
			temp.setState(Investment.INVESTMENT_STATUS_metting);
			investmentService.updateInvestment(temp, operator);
		}
	}
}
