package com.guohuai.asset.manage.boot.cashtool.pool;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolDao;
import com.guohuai.asset.manage.boot.cashtool.log.CashToolLogService;
import com.guohuai.asset.manage.boot.enums.CashToolEventType;
import com.guohuai.asset.manage.component.exception.AMPException;

/**
 * 
 * <p>Title: CashtoolRevenueService.java</p>    
 * <p>本息兑付Service </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:20
 */
@Service
@Transactional
public class CashtoolRevenueService {
	@Autowired
	private CashtoolRevenueDao cashtoolRevenueDao;
	
	@Autowired
	private CashToolDao cashtoolDao;
	
	@Autowired
	private CashToolLogService cashtoolLogservice;

	/**
	 * 现金管理工具收益采集
	 * @Title: save 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param cashToolRevenueForm
	 * @return CashToolRevenue    返回类型
	 */
	public CashToolRevenue save(CashToolRevenueForm cashToolRevenueForm) {
		String cashtoolOid = cashToolRevenueForm.getCashtoolOid();
		if (StringUtils.isBlank(cashtoolOid))
			throw AMPException.getException("现金管理工具id不能为空");
		
		CashToolRevenue cashToolRevenue = new CashToolRevenue();
		BeanUtils.copyProperties(cashToolRevenueForm, cashToolRevenue);

		CashTool cashTool = this.cashtoolDao.findOne(cashtoolOid);
		if (null == cashTool)
			throw AMPException.getException("找不到id为[" + cashtoolOid + "]的现金管理工具");
		cashToolRevenue.setCashTool(cashTool);
		
		cashToolRevenue.setCreateTime(new Timestamp(System.currentTimeMillis()));
		cashToolRevenue.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		this.cashtoolLogservice.saveCashToolLog(cashTool, CashToolEventType.revenue, cashToolRevenueForm.getOperator()); // 现金管理工具收益采集
		
		return cashtoolRevenueDao.save(cashToolRevenue);
	}
}
