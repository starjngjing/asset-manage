package com.guohuai.asset.manage.boot.investment.meeting;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentMeetingListResp> list(HttpServletRequest request,
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
		InvestmentMeetingListResp resps = new InvestmentMeetingListResp(entitys);
		return new ResponseEntity<InvestmentMeetingListResp>(resps, HttpStatus.OK);
	}

	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentMeetingDetResp> detail(String oid) {
		InvestmentMeeting meeting = investmentMeetingService.getMeetingDet(oid);
		return new ResponseEntity<InvestmentMeetingDetResp>(new InvestmentMeetingDetResp(meeting), HttpStatus.OK);
	}

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

	@RequestMapping(value = "addMeeting", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> addMeeting(AddInvestmentMeetingForm form) {
		String operator = super.getLoginAdmin();
		investmentMeetingService.addMeeting(form, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	@RequestMapping(value = "meetingTarget", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<List<InvestmentMeetingAssetResp>> meetingTargetList(String oid) {
		List<InvestmentMeetingAssetResp> list = investmentMeetingAssetService.getInvestmentByMeeting(oid);
		return new ResponseEntity<List<InvestmentMeetingAssetResp>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "meetingTargetVoteDet", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<List<InvestmentVoteDetResp>> meetingTargetList(String meetingOid,
			String targetOid) {
		System.out.println(meetingOid);
		System.out.println(targetOid);
		List<InvestmentVoteDetResp> resp = investmentMeetingVoteService.getVoteDetByMeetingAndInvestment(meetingOid,
				targetOid);
		return new ResponseEntity<List<InvestmentVoteDetResp>>(resp, HttpStatus.OK);
	}

}
