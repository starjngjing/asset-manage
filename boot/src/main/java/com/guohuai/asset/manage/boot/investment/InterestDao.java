package com.guohuai.asset.manage.boot.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * <p>Title: InterestDao.java</p>    
 * <p>本息兑付Dao</p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:40
 */
public interface InterestDao
		extends JpaRepository<Interest, String>, JpaSpecificationExecutor<Interest> {

}
