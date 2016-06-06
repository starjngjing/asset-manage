package com.guohuai.asset.manage.boot.investment;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InvestmentDao extends JpaRepository<Investment, String>, JpaSpecificationExecutor<Investment> {
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
	
	/**
	 * 根据生命状态查询投资标的
	 * 
	 * @Title: getInvestmentByLifeState
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param name
	 * @return List<Object> 返回类型
	 */
	@Query("select ct.oid, ct.name from Investment ct where ct.lifeState = ?1")
	public List<Object> getInvestmentByLifeState(String lifeState);

	/**
	 * 增加持仓金额
	 * 
	 * @Title: IncHoldAmount
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param oid
	 * @param holdAmount
	 * @return Investment 返回类型
	 */
	@Modifying
	@Query("update Investment set holdAmount = holdAmount + ?2 where oid = ?1")
	public int IncHoldAmount(String oid, BigDecimal holdAmount);

	/**
	 * 增加申请金额
	 * 
	 * @Title: IncApplyAmount
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param oid
	 * @param applyAmount
	 * @return Investment 返回类型
	 */
	@Modifying
	@Query("update Investment set applyAmount = applyAmount + ?2 where oid = ?1")
	public int IncApplyAmount(String oid, BigDecimal applyAmount);
}
