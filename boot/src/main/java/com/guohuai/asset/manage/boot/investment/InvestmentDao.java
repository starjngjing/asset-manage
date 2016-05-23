package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface InvestmentDao
		extends JpaRepository<Investment, String>, JpaSpecificationExecutor<Investment> {
	/**
	 * 根据名称模糊查询投资标的
	 * 
	 * @Title: getCashToolByName
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param name
	 * @return List<Object> 返回类型
	 */
	@Query("select ct.oid, ct.name from Investment ct where ct.name like ?1")
	public List<Object> getInvestmentByName(String name);
}
