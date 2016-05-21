package com.guohuai.asset.manage.boot.investment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.guohuai.asset.manage.boot.file.File;
import com.guohuai.asset.manage.boot.file.FileService;
import com.guohuai.asset.manage.boot.file.SaveFileForm;
import com.guohuai.asset.manage.boot.investment.meeting.AddInvestmentMeetingForm;
import com.guohuai.asset.manage.boot.investment.meeting.SummaryDetResp;
import com.guohuai.asset.manage.boot.investment.meeting.SummaryFileDet;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

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

	@Autowired
	private FileService fileService;

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
			InvestmentMeetingUser entity = InvestmentMeetingUser.builder().investmentMeeting(meeting)
					.participantOid(oid).build();
			investmentMeetingUserDao.save(entity);
		}
	}

	private void addTargetStr(String targetStr, InvestmentMeeting meeting, String operator) throws Exception {
		String[] arr = targetStr.split(",");
		for (String oid : arr) {
			Investment temp = investmentService.getInvestmentDet(oid);
			InvestmentMeetingAsset entity = InvestmentMeetingAsset.builder().investment(temp).investmentMeeting(meeting)
					.build();
			entity.setInvestment(temp);
			entity.setInvestmentMeeting(meeting);
			investmentMeetingAssetDao.save(entity);
			temp.setState(Investment.INVESTMENT_STATUS_metting);
			investmentService.updateInvestment(temp, operator);
		}
	}

	/**
	 * 获得过会纪要列表
	 * 
	 * @param oid
	 */
	public List<SummaryDetResp> getSummary(String oid) {
		List<SummaryDetResp> resps = new ArrayList<SummaryDetResp>();
		InvestmentMeeting meeting = this.getMeetingDet(oid);
		if (null == meeting.getFkey()) {
			return resps;
		}
		List<File> files = fileService.list(meeting.getFkey());
		SummaryDetResp resp = new SummaryDetResp();
		resp.setMeetingOid(meeting.getOid());
		resp.setFkey(files.get(0).getFkey());
		resp.setUpdateTime(files.get(0).getUpdateTime());
		resp.setOperator(files.get(0).getOperator());
		resps.add(resp);
		return resps;
	}

	/**
	 * 上传过会纪要
	 */
	public void summaryUp(String oid, String filesStr, String operator) {
		InvestmentMeeting meeting = this.getMeetingDet(oid);
		if (null == meeting) {
			throw new RuntimeException();
		}
		String fkey = meeting.getFkey();
		if (StringUtils.isEmpty(meeting.getFkey())) {
			// 之前未上传纪要,创建一个uuid
			fkey = StringUtil.uuid();
			meeting.setFkey(fkey);
			this.updateMeeting(meeting, operator);
		}
		List<SummaryFileDet> lists = JSONArray.parseArray(filesStr, SummaryFileDet.class);
		List<SaveFileForm> forms = new ArrayList<SaveFileForm>();
		for (SummaryFileDet file : lists) {
			SaveFileForm form = new SaveFileForm();
			form.setName(file.getName());
			form.setFurl(file.getUrl());
			form.setOid(StringUtil.uuid());
			form.setSize(file.getSize());
			forms.add(form);
		}
		fileService.merge(forms, fkey, "", operator);
	}

	/**
	 * 删除过会纪要
	 * 
	 * @param oid
	 * @param operator
	 */
	public void deleteSummary(String oid, String operator) {
		InvestmentMeeting meeting = this.getMeetingDet(oid);
		if (null == meeting) {
			throw new RuntimeException();
		}
		if (null != meeting.getFkey()) {
			fileService.batchDelete(meeting.getFkey(), operator);
			meeting.setFkey(null);
			this.updateMeeting(meeting, operator);
		}
	}

	/**
	 * 启动过会
	 * 
	 * @param oid
	 * @param operator
	 * @return
	 */
	public InvestmentMeeting openMeeting(String oid, String operator) {
		InvestmentMeeting meeting = this.getMeetingDet(oid);
		if (!InvestmentMeeting.MEETING_STATE_NOTOPEN.equals(meeting.getState())
				&& !InvestmentMeeting.MEETING_STATE_STOP.equals(meeting.getState())) {
			throw new RuntimeException();
		}
		meeting.setState(InvestmentMeeting.MEETING_STATE_OPENING);
		this.updateMeeting(meeting, operator);
		return meeting;
	}

	/**
	 * 
	 * 暂停过会
	 * 
	 * @param oid
	 * @param operator
	 * @return
	 */
	public InvestmentMeeting stopMeeting(String oid, String operator) {
		InvestmentMeeting meeting = this.getMeetingDet(oid);
		if (!InvestmentMeeting.MEETING_STATE_OPENING.equals(meeting.getState())) {
			throw new RuntimeException();
		}
		meeting.setState(InvestmentMeeting.MEETING_STATE_STOP);
		this.updateMeeting(meeting, operator);
		return meeting;
	}
}
