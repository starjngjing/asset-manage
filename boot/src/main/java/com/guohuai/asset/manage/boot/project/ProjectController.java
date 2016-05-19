
package com.guohuai.asset.manage.boot.project;

import java.util.List;

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

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/ams/boot/project")
@Slf4j
public class ProjectController extends BaseController {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectService projectService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get", method = RequestMethod.POST)
	public CommonResp get(String oid) {
		CommonResp cr = new CommonResp();
		cr.setErrorCode(1);
		cr.getRows().add((Project) projectDao.findOne(oid));
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
		Page<Project> pagedata = projectService.list(spec, page - 1, size, sortDirection, sortField);
		ProjectListResp resp = new ProjectListResp(pagedata);
		return new ResponseEntity<ProjectListResp>(resp, HttpStatus.OK);
	}

	/**
	 * 创建底层项目
	 * @Title: save 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param approvalReq
	 * @return CommonResp    返回类型
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResp save(ProjectForm approvalReq) {

		String loginId = null; 
		try {
			loginId = super.getLoginAdmin();
		} catch (Exception e) {
			log.error("获取操作员失败, 原因: " + e.getMessage());
		}
		Project prj = projectService.save(approvalReq, loginId, loginId);
		return CommonResp.builder().errorCode(1).errorMessage("保存底层项目成功！").attached(prj.getOid()).build();
	}

	/**
	 * 根据标的id查询底层项目
	 * @Title: getByTargetId 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param targetOid
	 * @return
	 * @return CommonResp    返回类型 
	 */
	@ApiOperation(value = "根据标的id查询底层项目")
	@RequestMapping(value = "getByTargetId")
	public CommonResp getByTargetId(@RequestParam(required = true) String targetOid) {
		List<Project> list = this.projectService.findByTargetId(targetOid);
		return CommonResp.builder().errorMessage("保存成功！").rows(list).total(null == list ? 0 : list.size()).build();
	}

}
