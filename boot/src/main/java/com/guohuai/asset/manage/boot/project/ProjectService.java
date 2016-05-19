package com.guohuai.asset.manage.boot.project;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.investment.InvestmentDao;
import com.guohuai.asset.manage.component.exception.AMPException;

@Service
@Transactional
public class ProjectService {

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private InvestmentDao investmentDao;

	public Page<Project> list(Specification<Project> spec, int page, int size, String sortDirection, String sortField) {
		Order order = new Order(Direction.valueOf(sortDirection.toUpperCase()), sortField);
		Pageable pageable = new PageRequest(page, size, new Sort(order));
		Page<Project> pagedata = this.projectDao.findAll(spec, pageable);
		return pagedata;
	}

	public Project get(String oid) {
		Project approv = this.projectDao.findOne(oid);
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
	public Project save(ProjectForm approv) {
		if (null == approv)
			throw AMPException.getException("底层项目不能为空");
		String targetOid = approv.getTargetOid();
		if (StringUtils.isBlank(targetOid))
			throw AMPException.getException("投资标的id不能为空");

		Project prj = new Project();
		BeanUtils.copyProperties(approv, prj);

		Investment investment = this.investmentDao.findOne(targetOid);
		if (null == investment)
			throw AMPException.getException("找不到id为[" + targetOid + "]的投资标的");
		prj.setInvestment(investment);
		
		prj.setCreateTime(new Timestamp(System.currentTimeMillis()));
		prj.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		return this.projectDao.save(prj);
	}
	
	/**
	 * 删除底层项目
	 * @Title: deleteByTargetOidAndOid 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param oid void    返回类型
	 */
	public void deleteByTargetOidAndOid(String targetOid, String oid) {
		this.projectDao.deleteByTargetOidAndOid(targetOid, oid);
	}
	
	/**
	 * 删除底层项目
	 * @Title: deleteByTargetOid 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param oid void    返回类型
	 */
	public void deleteByTargetOid(String targetOid) {
		this.projectDao.deleteByTargetOid(targetOid);
	}

	/**
	 * 通过标的id查询底层项目
	 * 
	 * @Title: findByTargetId
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param targetOid
	 * @return
	 * @return List<Project> 返回类型
	 */
	public List<Project> findByTargetId(String targetOid) {
		return this.projectDao.findByTargetOid(targetOid);
	}

}
