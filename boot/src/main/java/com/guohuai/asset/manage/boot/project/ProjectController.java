
package com.guohuai.asset.manage.boot.project;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.asset.manage.component.resp.CommonResp;
import com.guohuai.asset.manage.component.web.BaseController;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

/**
 * @ClassName: ProjectController
 * @Description: 立项管理
 * @author vania
 * @date 2016年3月25日 上午10:08:44
 *
 */
@RestController
@RequestMapping("/asset/hill/project")
public class ProjectController extends BaseController {
	@Autowired
	private ProjectDao approvalDao;
	@Autowired
	private ProjectService approvalService;

	@RequestMapping(value = "get", method = RequestMethod.POST)
	public CommonResp get(String oid) {
		CommonResp cr = new CommonResp();
		cr.setErrorCode(1);
		cr.getRows().add(approvalDao.findOne(oid));
		return cr;
	}

	@RequestMapping(value = "orderlist", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody ResponseEntity<ProjectListResp> orderlist(HttpServletRequest request,
			@And({ @Spec(path = "projectName", spec = Like.class), @Spec(path = "projectManager", spec = Equal.class),
					@Spec(path = "pjType", params = "pjType", spec = Equal.class) }) Specification<Project> spec,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int size, @RequestParam(defaultValue = "desc") String sortDirection,
			@RequestParam(defaultValue = "updateTime") String sortField) {
		if (page < 1) {
			page = 1;
		}
		if (size <= 0) {
			size = 50;
		}
		Page<Project> pagedata = approvalService.list(spec, page - 1, size, sortDirection, sortField);
		ProjectListResp resp = new ProjectListResp(pagedata);
		return new ResponseEntity<ProjectListResp>(resp, HttpStatus.OK);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResp save(ProjectReq approvalReq) {

		String oid = approvalReq.getOid();
		if (oid != null && !"".equals(oid.trim())) {
			Project asOld = approvalDao.findOne(oid);
		
			Project asNew = Project.builder().projectName(approvalReq.getProjectName()).projectManager(approvalReq.getProjectManager()).projectCity(approvalReq.getCityName())
					.endUseAmount(new BigDecimal(approvalReq.getEndUseAmount())).financialType1(approvalReq.getFinancialType1())
					.financialType2(approvalReq.getFinancialType2()).isPhasedrelease(approvalReq.getIsPhasedrelease()).isStockCoopOrg(approvalReq.getIsStockCoopOrg()).orgName(approvalReq.getOrgName())
					.pjSources(approvalReq.getPjSources()).projectType(approvalReq.getPjType()).relatedParty(approvalReq.getRelatedParty()).subsCount(approvalReq.getSubsCount())
					.tradeCredit(approvalReq.getTradeCredit()).usedAmount(new BigDecimal(approvalReq.getUsedAmount())).updateTime(new Timestamp(System.currentTimeMillis())).build();
			asNew.setOid(asOld.getOid());
			asNew.setCreateTime(asOld.getCreateTime());
//			approvalDao.save(asNew);
			approvalService.save(asNew);
			return CommonResp.builder().errorCode(1).errorMessage("更新成功").build();
		} else {
			Project approval = Project.builder().projectName(approvalReq.getProjectName()).projectManager(approvalReq.getProjectManager()).projectCity(approvalReq.getCityName())
					.endUseAmount(new BigDecimal(approvalReq.getEndUseAmount())).financialType1(approvalReq.getFinancialType1())
					.financialType2(approvalReq.getFinancialType2()).isPhasedrelease(approvalReq.getIsPhasedrelease()).isStockCoopOrg(approvalReq.getIsStockCoopOrg()).orgName(approvalReq.getOrgName())
					.pjSources(approvalReq.getPjSources()).projectType(approvalReq.getPjType()).relatedParty(approvalReq.getRelatedParty()).subsCount(approvalReq.getSubsCount())
					.tradeCredit(approvalReq.getTradeCredit()).usedAmount(new BigDecimal(approvalReq.getUsedAmount())).createTime(new Timestamp(System.currentTimeMillis()))
					.businesstype(approvalReq.getBusinesstype()).build();
//			approvalDao.save(approval);
			approvalService.save(approval);
			return CommonResp.builder().errorCode(1).errorMessage("保存成功！").attached(approval.getOid()).build();
		}
	}

}