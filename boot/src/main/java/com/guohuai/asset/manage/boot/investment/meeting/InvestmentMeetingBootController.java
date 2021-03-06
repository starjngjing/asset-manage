package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentListResp;
import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingAssetService;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingService;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingVoteService;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

/**
 * 投资标的过会管理
 * 
 * @author lirong
 *
 */
@RestController
@RequestMapping(value = "/ams/target/targetMeeting", produces = "application/json;charset=UTF-8")
@Slf4j
public class InvestmentMeetingBootController extends BaseController {

	@Autowired
	private InvestmentMeetingService investmentMeetingService;
	@Autowired
	private InvestmentService investmentService;
	@Autowired
	private InvestmentMeetingAssetService investmentMeetingAssetService;
	@Autowired
	private InvestmentMeetingVoteService investmentMeetingVoteService;

	/**
	 * 过会会议列表
	 * 
	 * @param request
	 * @param spec
	 * @param page
	 * @param rows
	 * @param sortField
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<MeetingListResp> list(HttpServletRequest request,
			@And({ @Spec(params = "sn", path = "sn", spec = Equal.class),
					@Spec(params = "title", path = "title", spec = Like.class),
					@Spec(params = "state", path = "state", spec = Equal.class) }) Specification<InvestmentMeeting> spec,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		Page<InvestmentMeeting> entitys = investmentMeetingService.getMeetingList(spec, pageable);
		MeetingListResp resps = new MeetingListResp(entitys);
		return new ResponseEntity<MeetingListResp>(resps, HttpStatus.OK);
	}

	/**
	 * 过会会议详情
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<MeetingDetResp> detail(@RequestParam(required = true) String oid) {
		InvestmentMeeting meeting = investmentMeetingService.getMeetingDet(oid);
		return new ResponseEntity<MeetingDetResp>(new MeetingDetResp(meeting), HttpStatus.OK);
	}

	/**
	 * 待过会标的列表
	 * 
	 * @param request
	 * @param spec
	 * @param page
	 * @param rows
	 * @param sortField
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "targetList", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentListResp> findInvestmentList(HttpServletRequest request,
			@And({ @Spec(params = "name", path = "name", spec = Like.class) }) Specification<Investment> spec,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Specification<Investment> typeSpec = new Specification<Investment>() {
			@Override
			public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("state").as(String.class), Investment.INVESTMENT_STATUS_waitMeeting);
			}
		};
		spec = Specifications.where(spec).and(typeSpec);
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		Page<Investment> entitys = investmentService.getInvestmentList(spec, pageable);
		InvestmentListResp resps = new InvestmentListResp(entitys);
		return new ResponseEntity<InvestmentListResp>(resps, HttpStatus.OK);
	}

	/**
	 * 创建会议
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "addMeeting", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> addMeeting(@Valid AddInvestmentMeetingForm form) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.addMeeting(form, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 会议的标的列表
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "meetingTarget", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<List<MeetingInvestmentDetResp>> meetingTargetList(
			@RequestParam(required = true) String oid) {
		List<MeetingInvestmentDetResp> list = investmentMeetingAssetService.getInvestmentByMeeting(oid);
		return new ResponseEntity<List<MeetingInvestmentDetResp>>(list, HttpStatus.OK);
	}

	/**
	 * 会议的标的表决详情列表
	 * 
	 * @param meetingOid
	 * @param targetOid
	 * @return
	 */
	@RequestMapping(value = "meetingTargetVoteDet", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<List<VoteDetResp>> meetingTargetList(
			@RequestParam(required = true) String meetingOid, @RequestParam(required = true) String targetOid) {
		List<VoteDetResp> resp = investmentMeetingVoteService.getVoteDetByMeetingAndInvestment(meetingOid, targetOid);
		return new ResponseEntity<List<VoteDetResp>>(resp, HttpStatus.OK);
	}

	/**
	 * 会议纪要详情
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "summaryDet", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<SummaryListResp> summaryList(@RequestParam(required = true) String oid) {
		List<SummaryDetResp> resp = investmentMeetingService.getSummary(oid);
		return new ResponseEntity<SummaryListResp>(new SummaryListResp(resp), HttpStatus.OK);
	}

	/**
	 * 上传会议纪要
	 * 
	 * @param files
	 * @param meetingOid
	 * @return
	 */
	@RequestMapping(value = "summaryUp", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> summaryUp(@RequestParam(required = true) String files,
			@RequestParam(required = true) String meetingOid) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.summaryUp(meetingOid, files, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 删除会议纪要
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "summaryDetele", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> summaryDelete(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.deleteSummary(oid, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 启动过会
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "open", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> open(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.openMeeting(oid, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 暂停过会
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "stop", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> stop(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.stopMeeting(oid, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 会议确认
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "finish", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> finish(@Valid MeetingFinishForm form) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.finishMeeting(form, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 标的最新会议信息
	 * 
	 * @param targetoid
	 * @return
	 */
	@RequestMapping(value = "targetMeeting", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<MeetingDetResp> detilForTarget(
			@RequestParam(required = true) String investmentOid) {
		InvestmentMeeting meeting = investmentMeetingAssetService.getNewMeetingByInvestment(investmentOid);
		return new ResponseEntity<MeetingDetResp>(new MeetingDetResp(meeting), HttpStatus.OK);
	}

}
