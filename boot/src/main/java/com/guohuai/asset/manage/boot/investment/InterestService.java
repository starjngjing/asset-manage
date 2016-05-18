package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <p>Title: InterestService.java</p>    
 * <p>本息兑付Service </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:20
 */
@Service
@Transactional
public class InterestService {
	@Autowired
	InterestDao interestDao;

	@Transactional
	public Interest save(Interest interest) {
		return interestDao.save(interest);
	}
}
