package com.guohuai.asset.manage.boot.approval;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApprovalDao extends JpaRepository<Approval, Serializable>, JpaSpecificationExecutor<Approval> {


	@Query(value="update T_GAM_APPROVAL set state = 'DELETED' where oid=?1", nativeQuery = true)
	@Modifying
	@Transactional
	public void delete_(String oid);
	
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
	public List<Approval> findByTargetOid(String targetOid);
	
}
