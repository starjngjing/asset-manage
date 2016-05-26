package com.guohuai.asset.manage.boot.cashtool.pool;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolDao;
import com.guohuai.asset.manage.boot.cashtool.CashToolService;
import com.guohuai.asset.manage.boot.cashtool.log.CashToolLogService;

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
					Expression<String> expType = root.get("etfLof").as(String.class); // 标的类型
					predicate.add(expType.in(cashtoolTypes));// type =  PREPARE
				}
				
				Expression<String> exp = root.get("state").as(String.class); // 标的生命周期					
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
}
