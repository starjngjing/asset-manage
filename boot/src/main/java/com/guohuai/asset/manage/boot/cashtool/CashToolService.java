/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.cashtool;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


/**    
 * <p>Title: CashToolService.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月17日 下午2:34:52   
 */   
@Service
@Transactional
public class CashToolService {

	@Autowired
	private CashToolDao cashToolDao;

	/**
	 * 分页查询现金管理工具
	 * @Title: getCashToolList 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param spec
	 * @param pageable
	 * @return
	 * @return Page<CashTool>    返回类型 
	 */
	public Page<CashTool> getCashToolList(Specification<CashTool> spec, Pageable pageable) {
		return cashToolDao.findAll(spec, pageable);
	}

}
