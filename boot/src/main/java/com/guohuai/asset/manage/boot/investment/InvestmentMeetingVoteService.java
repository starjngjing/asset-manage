package com.guohuai.asset.manage.boot.investment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.investment.meeting.InvestmentVoteDetResp;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;

@Service
@Transactional
public class InvestmentMeetingVoteService {

	@Autowired
	private InvestmentMeetingVoteDao investmentMeetingVoteDao;

	@Autowired
	private InvestmentMeetingService investmentMeetingService;

	@Autowired
	private InvestmentService investmentService;

	@Autowired
	private AdminSdk adminSdk;

	@Autowired
	private InvestmentMeetingUserService investmentMeetingUserService;

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

	/**
	 * 根据会议和标的查询投票详情
	 * 
	 * @param meetingOid
	 * @param investmentOid
	 */
	public List<InvestmentVoteDetResp> getVoteDetByMeetingAndInvestment(String meetingOid, String investmentOid) {
		List<InvestmentVoteDetResp> resps = new ArrayList<InvestmentVoteDetResp>();
		InvestmentMeeting meeting = investmentMeetingService.getMeetingDet(meetingOid);
		Investment investment = investmentService.getInvestmentDet(investmentOid);
		List<InvestmentMeetingUser> userList = investmentMeetingUserService.getMeetingUserByMeeting(meeting);
		for (InvestmentMeetingUser user : userList) {
			List<InvestmentMeetingVote> votes = investmentMeetingVoteDao
					.findByInvestmentMeetingAndInvestmentAndInvestmentMeetingUser(meeting, investment, user);
			AdminObj userInfo = adminSdk.getAdmin(user.getParticipantOid());
			InvestmentVoteDetResp resp = new InvestmentVoteDetResp();
			if (votes != null && votes.size() > 0) {
				resp.setState(votes.get(0).getState());
				resp.setTime(votes.get(0).getVoteTime());
			} else {
				resp.setState(InvestmentMeetingVote.VOTE_STATUS_notvote);
			}
			resp.setName(userInfo.getName());
			resps.add(resp);
		}
		return resps;
	}
}
