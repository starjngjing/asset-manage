package com.guohuai.asset.manage.boot.investment.vote;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentMeeting;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingAssetService;
import com.guohuai.asset.manage.boot.investment.meeting.MeetingInvestmentDetResp;
import com.guohuai.asset.manage.boot.investment.meeting.MeetingInvestmentListResp;
import com.guohuai.asset.manage.boot.investment.meeting.MeetingListResp;
import com.guohuai.asset.manage.component.web.BaseController;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

/**
 * 投资标的过会表决
 * 
 * @author lirong
 *
 */
@RestController
@RequestMapping(value = "/ams/target/targetVote", produces = "application/json;charset=UTF-8")
public class InvestmentMeetingVoteBootController extends BaseController{
	
	@Autowired
	private InvestmentMeetingAssetService investmentMeetingAssetService;

	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<MeetingInvestmentListResp> list() {
		String operator = super.getLoginAdmin();
		List<MeetingInvestmentDetResp> list = investmentMeetingAssetService.getInvestmentByParticipant(operator);
		MeetingInvestmentListResp resps = new MeetingInvestmentListResp(list);
		return new ResponseEntity<MeetingInvestmentListResp>(resps, HttpStatus.OK);
	}
}
