package com.guohuai.asset.manage.boot.project;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guohuai.asset.manage.component.exception.AMPException;

@Service
public class ProjectService {

	@Autowired
	private ProjectDao approvalDao;

	@Transactional
	public Page<Project> list(Specification<Project> spec, int page, int size, String sortDirection, String sortField) {
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page, size, new Sort(order));
		Page<Project> pagedata = this.approvalDao.findAll(spec, pageable);
		return pagedata;
	}

	@Transactional
	public Project get(String oid) {
		Project approv = this.approvalDao.findOne(oid);
		if (null == approv) {
			throw AMPException.getException("未知的项目ID");
		}
		return approv;
	}

	/**
	 * 保存底层项目
	 * 
	 * @Title: save
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param approv
	 * @return
	 * @return Approval 返回类型
	 */
	@Transactional
	public Project save(Project approv) {
		if (null == approv)
			throw AMPException.getException("底层项目不能为空");
		if (StringUtils.isBlank(approv.getTargetOid())) {
			throw AMPException.getException("关联标的不能为空");
		}
		return this.approvalDao.save(approv);
	}

	/**
	 * 通过标的id查询底层项目
	 * 
	 * @Title: findByInvestmentId
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param targetOid
	 * @return
	 * @return List<Approval> 返回类型
	 */
	public List<Project> findByInvestmentId(String targetOid) {
		return this.approvalDao.findByTargetOid(targetOid);
	}

}
