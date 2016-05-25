package com.guohuai.asset.manage.boot.investment.manage;

import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.guohuai.asset.manage.boot.enums.TargetEventType;
import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentDetResp;
import com.guohuai.asset.manage.boot.investment.InvestmentListResp;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingCheck;
import com.guohuai.asset.manage.boot.investment.InvestmentMeetingCheckService;
import com.guohuai.asset.manage.boot.investment.InvestmentService;
import com.guohuai.asset.manage.boot.investment.log.InvestmentLogService;
import com.guohuai.asset.manage.component.web.BaseController;
import com.guohuai.asset.manage.component.web.view.BaseResp;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

/**
 * 投资标的管理
 * 
 * @author lirong
 *
 */
@RestController
@RequestMapping(value = "/ams/target/targetManage", produces = "application/json;charset=UTF-8")
public class InvestmentManageBootController extends BaseController {

	@Autowired
	private InvestmentService investmentService;
	@Autowired
	private InvestmentLogService investmentLogService;
	@Autowired
	private InvestmentMeetingCheckService investmentMeetingCheckService;

	/**
	 * 投资标的列表
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
	public @ResponseBody ResponseEntity<InvestmentListResp> list(HttpServletRequest request,
			@And({ @Spec(params = "name", path = "name", spec = Like.class),
					@Spec(params = "type", path = "type", spec = Equal.class),
					@Spec(params = "state", path = "state", spec = Equal.class) }) Specification<Investment> spec,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false, defaultValue = "updateTime") String sortField,
			@RequestParam(required = false, defaultValue = "desc") String sort) {
		Direction sortDirection = Direction.DESC;
		if (!"desc".equals(sort)) {
			sortDirection = Direction.ASC;
		}
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Order(sortDirection, sortField)));
		Page<Investment> entitys = investmentService.getInvestmentList(spec, pageable);
		InvestmentListResp resps = new InvestmentListResp(entitys);
		return new ResponseEntity<InvestmentListResp>(resps, HttpStatus.OK);
	}

	/**
	 * 投资标的详情
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentDetResp> detail(@RequestParam(required = true) String oid) {
		Investment entity = investmentService.getInvestmentDet(oid);
		InvestmentDetResp resp = new InvestmentDetResp(entity);
		return new ResponseEntity<InvestmentDetResp>(resp, HttpStatus.OK);
	}

	/**
	 * 新建投资标的
	 * 
	 * @param investment
	 * @return
	 */
	@RequestMapping(value = "add", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> add(@Valid InvestmentManageForm form) {
		String operator = super.getLoginAdmin();
		Investment investment = investmentService.createInvestment(form);
		investment.setState(Investment.INVESTMENT_STATUS_waitPretrial);
		investment = investmentService.saveInvestment(investment, operator);
		investmentLogService.saveInvestmentLog(investment, TargetEventType.create, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 编辑投资标的
	 * 
	 * @param investment
	 * @return
	 */
	@RequestMapping(value = "edit", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> edit(@Valid InvestmentManageForm form) {
		String operator = super.getLoginAdmin();
		Investment investment = investmentService.getInvestmentDet(form.getOid());
		if (!Investment.INVESTMENT_STATUS_waitPretrial.equals(investment.getState())
				&& !Investment.INVESTMENT_STATUS_reject.equals(investment.getState())) {
			throw new RuntimeException();
		}
		Investment temp = investmentService.createInvestment(form);
		temp.setState(investment.getState());
		temp.setCreateTime(investment.getCreateTime());
		temp.setCreator(investment.getCreator());
		investment = investmentService.updateInvestment(temp, operator);
		investmentLogService.saveInvestmentLog(investment, TargetEventType.edit, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 提交预审
	 * 
	 * @param investment
	 * @return
	 */
	@RequestMapping(value = "examine", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> examine(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		Investment investment = investmentService.getInvestmentDet(oid);
		if (!Investment.INVESTMENT_STATUS_waitPretrial.equals(investment.getState())
				&& !Investment.INVESTMENT_STATUS_reject.equals(investment.getState())) {
			// 标的状态不是待预审或驳回不能提交预审
			throw new RuntimeException();
		}
		investment.setState(Investment.INVESTMENT_STATUS_pretrial);
		investmentService.saveInvestment(investment, operator);
		investmentLogService.saveInvestmentLog(investment, TargetEventType.check, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 作废
	 * 
	 * @param investment
	 * @return
	 */
	@RequestMapping(value = "invalid", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> invalid(@RequestParam(required = true) String oid) {
		String operator = super.getLoginAdmin();
		Investment investment = investmentService.getInvestmentDet(oid);
		investment.setState(Investment.INVESTMENT_STATUS_invalid);
		investmentService.saveInvestment(investment, operator);
		investmentLogService.saveInvestmentLog(investment, TargetEventType.invalid, operator);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

	/**
	 * 根据标的oid获得未确认检查项列表
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "checkListNotConfrim", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentCheckListResp> checkListNotConfrim(
			@RequestParam(required = true) String oid) {
		List<InvestmentCheckDetResp> checkList = investmentMeetingCheckService.getMeetingCheckListByInvestmentOid(oid,
				InvestmentMeetingCheck.MEETINGCHEC_STATUS_notcheck);
		return new ResponseEntity<InvestmentCheckListResp>(new InvestmentCheckListResp(checkList), HttpStatus.OK);
	}

	/**
	 * 根据标的oid获得已确认检查项列表
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "checkListConfrim", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentCheckListResp> checkListConfrim(
			@RequestParam(required = true) String oid) {
		List<InvestmentCheckDetResp> checkList = investmentMeetingCheckService.getMeetingCheckListByInvestmentOid(oid,
				InvestmentMeetingCheck.MEETINGCHEC_STATUS_check);
		return new ResponseEntity<InvestmentCheckListResp>(new InvestmentCheckListResp(checkList), HttpStatus.OK);
	}

	/**
	 * 根据标的oid获得所有检查项列表
	 * 
	 * @param oid
	 * @return
	 */
	@RequestMapping(value = "checkListAll", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<InvestmentCheckListResp> checkList(@RequestParam(required = true) String oid) {
		List<InvestmentCheckDetResp> checkList = investmentMeetingCheckService.getMeetingCheckListByInvestmentOid(oid,
				null);
		return new ResponseEntity<InvestmentCheckListResp>(new InvestmentCheckListResp(checkList), HttpStatus.OK);
	}

	/**
	 * 过会检查项确认
	 * 
	 * @param checkConditions
	 * @return
	 */
	@RequestMapping(value = "confirmCheckList", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<BaseResp> confirmCheckList(
			@RequestParam(required = true) String checkConditions) {
		String operatpr = super.getLoginAdmin();
		List<InvestmentCheckListConfirmForm> form = JSONArray.parseArray(checkConditions,
				InvestmentCheckListConfirmForm.class);
		investmentMeetingCheckService.confirmCheckList(form, operatpr);
		return new ResponseEntity<BaseResp>(new BaseResp(), HttpStatus.OK);
	}

}
