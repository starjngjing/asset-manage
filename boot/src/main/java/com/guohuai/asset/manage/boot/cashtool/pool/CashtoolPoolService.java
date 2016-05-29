package com.guohuai.asset.manage.boot.cashtool.pool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolDao;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.boot.cashtool.log.CashToolLogService;
import com.guohuai.asset.manage.boot.enums.CashToolEventType;
import com.guohuai.asset.manage.component.exception.AMPException;

/**
 * 
 * <p>Title: CashtoolPoolService.java</p>    
 * <p>现金管理工具备选库Service </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月18日 下午3:31:20
 */
@Service
@Transactional
public class CashtoolPoolService {
	@Autowired
	private CashtoolRevenueDao cashtoolRevenueDao;
	
	@Autowired
	private CashToolDao cashtoolDao;
	
	@Autowired
	private CashToolLogService cashtoolLogservice;
	
	@Autowired
	private CashToolService cashtoolservice;

	/**
	 * 查询指定类型的所有现金管理类工具
	 * @Title: getCollecting 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param cashtoolTypes
	 * @return List<CashTool>    返回类型
	 */
	public List<CashTool> getCollecting(final String[] cashtoolTypes) {
		Specification<CashTool> spec = new Specification<CashTool>() {
			
			@Override
			public Predicate toPredicate(Root<CashTool> root, CriteriaQuery<?> query, CriteriaBuilder cb) {	
				List<Predicate> predicate = new ArrayList<>();
				if (null != cashtoolTypes && cashtoolTypes.length > 0) {
					Expression<String> expType = root.get("etfLof").as(String.class); // 现金管理工具类型
					predicate.add(expType.in(cashtoolTypes));// type =  PREPARE
				}
				
				Expression<String> exp = root.get("state").as(String.class); // 现金管理工具状态					
				predicate.add(exp.in(new Object[] { CashTool.CASHTOOL_STATE_collecting }));//state = PREPARE
				
//				Expression<Date> expHa = root.get("collectEndDate").as(Date.class); // 募集截止日
//				Predicate p = cb.lessThanOrEqualTo(expHa, collectEndDate); //募集截止日 <= 指定日期	
//				predicate.add(p);		
				
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		};
		return cashtoolDao.findAll(spec);
	}
	
	
	/**
	 * 现金管理工具收益采集
	 * @Title: save 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param form
	 * @return CashToolRevenue    返回类型
	 */
	public CashToolRevenue cashToolRevenue(CashToolRevenueForm form) {
		String cashtoolOid = form.getCashtoolOid();
		if (StringUtils.isBlank(cashtoolOid))
			throw AMPException.getException("现金管理工具id不能为空");
		
		CashToolRevenue cr = new CashToolRevenue();
		BeanUtils.copyProperties(form, cr);

		CashTool cashTool = this.cashtoolDao.findOne(cashtoolOid);
		if (null == cashTool)
			throw AMPException.getException("找不到id为[" + cashtoolOid + "]的现金管理工具");
		cr.setCashTool(cashTool);
		
		cr.setCreateTime(new Timestamp(System.currentTimeMillis()));
		cr.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		this.cashtoolLogservice.saveCashToolLog(cashTool, CashToolEventType.revenue, form.getOperator()); // 现金管理工具收益采集
		
		this.cashtoolRevenueDao.deleteByDailyProfitDate(form.getDailyProfitDate());
		
		cr = cashtoolRevenueDao.save(cr);
		
		this.cashtoolDao.cashtoolRevenue(cashTool.getOid(), cr.getDailyProfitDate(), cr.getDailyProfit(), cr.getWeeklyYield());
		
		return cr;
	}
	
}
