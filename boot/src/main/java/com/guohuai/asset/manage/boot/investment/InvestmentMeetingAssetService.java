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

import com.guohuai.asset.manage.boot.investment.meeting.MeetingInvestmentDetResp;

@Service
@Transactional
public class InvestmentMeetingAssetService {

	@Autowired
	private InvestmentMeetingAssetDao investmentMeetingAssetDao;

	@Autowired
	private InvestmentMeetingService investmentMeetingService;

	@Autowired
	private InvestmentMeetingUserService investmentMeetingUserService;

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
	 * 根据会议获得标的
	 * 
	 * @param meeting
	 * @return
	 */
	public List<InvestmentMeetingAsset> getMeetingAssetListByMeeting(InvestmentMeeting meeting) {
		return investmentMeetingAssetDao.findByInvestmentMeeting(meeting);
	}

	/**
	 * 根据批量会议获得标的
	 * 
	 * @param meeting
	 * @return
	 */
	public List<InvestmentMeetingAsset> getMeetingAssetListByBathcMeeting(InvestmentMeeting[] meeting) {
		return investmentMeetingAssetDao.findByInvestmentMeetingIn(meeting);
	}

	/**
	 * 根据会议获得标的列表
	 * 
	 * @param meeting
	 * @return
	 */
	public List<MeetingInvestmentDetResp> getInvestmentByMeeting(String meetingOid) {
		InvestmentMeeting meeting = investmentMeetingService.getMeetingDet(meetingOid);
		List<InvestmentMeetingAsset> lists = this.getMeetingAssetListByMeeting(meeting);
		List<MeetingInvestmentDetResp> investments = new ArrayList<MeetingInvestmentDetResp>();
		for (InvestmentMeetingAsset temp : lists) {
			MeetingInvestmentDetResp entity = new MeetingInvestmentDetResp();
			BeanUtils.copyProperties(temp.getInvestment(), entity);
			investments.add(entity);
		}
		return investments;
	}

	/**
	 * 根据与会人查询标的列表
	 * 
	 * @param participantOid
	 * @param pageabl
	 * @return
	 */
	public List<MeetingInvestmentDetResp> getInvestmentByParticipant(String participantOid) {
		List<InvestmentMeetingUser> participantList = investmentMeetingUserService.getMeetingUserByUser(participantOid);
		List<InvestmentMeeting> openMeetingList = new ArrayList<InvestmentMeeting>();
		for (InvestmentMeetingUser temp : participantList) {
			if (InvestmentMeeting.MEETING_STATE_OPENING.equals(temp.getInvestmentMeeting().getState())) {
				openMeetingList.add(temp.getInvestmentMeeting());
			}
		}
		InvestmentMeeting[] opMeetingArr = (InvestmentMeeting[]) openMeetingList.toArray(new InvestmentMeeting[0]);
		List<InvestmentMeetingAsset> assetList = this.getMeetingAssetListByBathcMeeting(opMeetingArr);
		List<MeetingInvestmentDetResp> res = new ArrayList<MeetingInvestmentDetResp>();
		for (InvestmentMeetingAsset temp : assetList) {
			MeetingInvestmentDetResp resp = new MeetingInvestmentDetResp();
			BeanUtils.copyProperties(temp.getInvestment(), resp);
			resp.setMeetingOid(temp.getInvestmentMeeting().getOid());
			res.add(resp);
		}
		return res;
	}

}
