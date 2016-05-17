package com.guohuai.asset.manage.boot.project;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProjectDao extends JpaRepository<Project, Serializable>, JpaSpecificationExecutor<Project> {


	
	/**
	 * 根据投资标的id查询底层项目
	 * @Title: findByTargetOid 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param targetOid
	 * @return
	 * @return List<Approval>    返回类型 
	 */
	@Query(value = "from Project p where p.investment.oid = ?1")
	public List<Project> findByTargetOid(String targetOid);
	
}
