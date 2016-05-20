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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.log.CashToolLogService;
import com.guohuai.asset.manage.boot.enums.CashToolEventType;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * <p>
 * Title: CashToolService.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author vania
 * @version 1.0
 * @created 2016年5月17日 下午2:34:52
 */
@Service
@Transactional
public class CashToolService {

	@Autowired
	private CashToolDao cashToolDao;
	
	@Autowired
	private CashToolLogService cashToolLogService;

	public CashTool save(CashTool cashTool) {
		return this.cashToolDao.save(cashTool);
	}

	/**
	 * 根据oid查询现金管理工具
	 * 
	 * @Title: findByOid
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param oid
	 * @return CashTool 返回类型
	 */
	public CashTool findByOid(String oid) {
		return this.cashToolDao.findOne(oid);
	}

	/**
	 * 分页查询现金管理工具
	 * 
	 * @Title: getCashToolList
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param spec
	 * @param pageable
	 * @return
	 * @return Page<CashTool> 返回类型
	 */
	public Page<CashTool> getCashToolList(Specification<CashTool> spec, Pageable pageable) {
		return cashToolDao.findAll(spec, pageable);
	}

	/**
	 * form转entity
	 * 
	 * @param form
	 * @return
	 */
	public CashTool createInvestment(CashToolManageForm form) {
		CashTool entity = new CashTool();
		entity.setOid(StringUtil.uuid());
		try {
			BeanUtils.copyProperties(form, entity);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 移除出库
	 * 
	 * @Title: remove
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param oid
	 *            void 返回类型
	 */
	public void remove(String oid, String operator) {
		CashTool ct = this.cashToolDao.findOne(oid);
		ct.setState(CashTool.CASHTOOL_STATE_delete);
		ct.setOperator(operator);
		ct.setUpdateTime(DateUtil.getSqlCurrentDate());
		cashToolLogService.saveCashToolLog(oid, CashToolEventType.delete, operator);
		this.cashToolDao.save(ct);
	}

}
