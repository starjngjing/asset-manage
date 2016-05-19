package com.guohuai.asset.manage.boot.investment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 * <p>Title: InterestDao.java</p>    
 * <p>本息兑付Dao</p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:40
 */
public interface TargetIncomeDao
		extends JpaRepository<TargetIncome, String>, JpaSpecificationExecutor<TargetIncome> {
	/**
	 * 根据投资标的id查询本兮兑付记录
	 * @Title: findByTargetOid 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param targetOid
	 * @return
	 * @return List<Interest>    返回类型 
	 */
	@Query(value = "from Interest p where p.investment.oid = ?1")
	public List<TargetIncome> findByTargetOid(String targetOid);
}
