package com.guohuai.asset.manage.boot.duration.assetPool;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.acct.books.AccountBook;
import com.guohuai.asset.manage.boot.acct.books.AccountBookService;
import com.guohuai.asset.manage.boot.duration.assetPool.scope.ScopeService;
import com.guohuai.asset.manage.boot.duration.capital.calc.AssetPoolCalc;
import com.guohuai.asset.manage.boot.duration.capital.calc.AssetPoolCalcService;
import com.guohuai.asset.manage.boot.duration.capital.calc.ScheduleLog;
import com.guohuai.asset.manage.boot.duration.capital.calc.ScheduleLogService;
import com.guohuai.asset.manage.boot.duration.capital.calc.error.ErrorCalc;
import com.guohuai.asset.manage.boot.duration.capital.calc.error.ErrorCalcService;
import com.guohuai.asset.manage.boot.duration.capital.calc.fund.FundCalc;
import com.guohuai.asset.manage.boot.duration.capital.calc.fund.FundCalcService;
import com.guohuai.asset.manage.boot.duration.capital.calc.trust.TrustCalc;
import com.guohuai.asset.manage.boot.duration.capital.calc.trust.TrustCalcService;
import com.guohuai.asset.manage.boot.duration.order.fund.FundEntity;
import com.guohuai.asset.manage.boot.duration.order.fund.FundService;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustEntity;
import com.guohuai.asset.manage.boot.duration.order.trust.TrustService;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 存续期--资产池服务接口
 * @author star.zhu
 * 2016年5月16日
 */
@Service
public class AssetPoolService {
	
	@Autowired
	private AssetPoolDao assetPoolDao;
	
	@Autowired
	private ScopeService scopeService;
	@Autowired
	private FundService fundService;
	@Autowired
	private TrustService trustService;
	
	@Autowired
	private FundCalcService fundCalcService;
	@Autowired
	private TrustCalcService trustCalcService;
	@Autowired
	private AssetPoolCalcService poolCalcService;
	@Autowired
	private ErrorCalcService errorCalcService;
	@Autowired
	private ScheduleLogService logService;
	
//	@Autowired
//	private IncomeDocumentService incomeService;
	@Autowired
	private AccountBookService accountService;
	
	/**
	 * 新建资产池
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void createPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = new AssetPoolEntity();
		try {
			entity.setOid(StringUtil.uuid());
			entity.setName(form.getName());
			entity.setScale(form.getScale());
			entity.setCashPosition(form.getScale());
			entity.setCashRate(form.getCashRate());
			entity.setCashtoolRate(form.getCashtoolRate());
			entity.setTargetRate(form.getTargetRate());
			entity.setCashFactRate(new BigDecimal(100));
			entity.setState(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_01"));
			entity.setCreater(uid);
			entity.setCreateTime(DateUtil.getSqlCurrentDate());
			
			assetPoolDao.save(entity);
			
			for (String s : form.getScopes()) {
				scopeService.save(entity, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新建审核
	 * @param operation
	 * 				yes：同意
	 * 				no：不同意
	 * @param oid
	 * @param uid
	 */
	@Transactional
	public void auditPool(String operation, String oid, String uid) {
		AssetPoolEntity entity = assetPoolDao.findOne(oid);
		if ("yes".equals(operation)) {
			entity.setState(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_02"));
		} else {
			entity.setState(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_03"));
		}
	}

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	@Transactional
	public Object[] getAllList(Specification<AssetPoolEntity> spec, Pageable pageable) {
		List<AssetPoolForm> formList = Lists.newArrayList();
		Page<AssetPoolEntity> entityList = assetPoolDao.findAll(spec, pageable);
		AssetPoolForm form = null;
		if (null != entityList.getContent() && entityList.getContent().size() > 0) {
			for (AssetPoolEntity entity : entityList.getContent()) {
				form = new AssetPoolForm();
				try {
					BeanUtils.copyProperties(form, entity);
					form.setState(entity.getState().equals(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_01")) 
							?  "0" 
									: entity.getState().equals(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_02")) 
									? "1" : "-1");
				} catch (Exception e) {
					e.printStackTrace();
				}
				formList.add(form);
			}
		}
		Object[] obj = new Object[2];
		obj[0] = entityList.getTotalElements();
		obj[1] = formList;
		
		return obj;
	}
	
	/**
	 * 获取所有资产池的名称列表，包含id
	 * @return
	 */
	@Transactional
	public List<JSONObject> getAllNameList() {
		List<JSONObject> jsonObjList = Lists.newArrayList();
		List<Object> objList = assetPoolDao.findAllNameList();
		if (!objList.isEmpty()) {
			Object[] obs = null;
			JSONObject jsonObj = null;
			for (Object obj : objList) {
				obs = (Object[]) obj;
				jsonObj = new JSONObject();
				jsonObj.put("oid", obs[0]);
				jsonObj.put("name", obs[1]);
				
				jsonObjList.add(jsonObj);
			}
		}
		
		return jsonObjList;
	}
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	@Transactional
	public AssetPoolEntity getByOid(String pid) {
		AssetPoolEntity entity = assetPoolDao.findOne(pid);
		
		return entity;
	}
	
	/**
	 * 保存
	 * @param entity
	 */
	@Transactional
	public void save(AssetPoolEntity entity) {
		assetPoolDao.save(entity);
	}
	
	/**
	 * 根据资产池id获取对应的资产池详情
	 * @param pid
	 * @return
	 */
	@Transactional
	public AssetPoolForm getPoolByOid(String pid) {
		AssetPoolForm form = new AssetPoolForm();
		AssetPoolEntity entity = new AssetPoolEntity();
		if (null == pid || "".equals(pid)) {
			entity = assetPoolDao.getLimitOne();
			pid = entity.getOid();
		} else {
			entity = this.getByOid(pid);
		}
		String[] scopes = scopeService.getScopes(pid);
		try {
			BeanUtils.copyProperties(form, entity);
			form.setScopes(scopes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 获取收益数据
		Map<String, AccountBook> accountMap = accountService.find(pid, "2201", "2301", "300101", "300102");
		if (null != accountMap) {
			if (accountMap.containsKey("2201")) {
				form.setUnDistributeProfit(accountMap.get("2201").getBalance());
			}
			if (accountMap.containsKey("2301")) {
				form.setPayFeigin(accountMap.get("2301").getBalance());
			}
			if (accountMap.containsKey("300101")) {
				form.setInvestorProfit(accountMap.get("300101").getBalance());
			}
			if (accountMap.containsKey("300102")) {
				form.setSpvProfit(accountMap.get("300102").getBalance());
			}
		}
		
		return form;
	}
	
	/**
	 * 当pid为空的时候，默认获取第一个资产池
	 * @param pid
	 * @return
	 */
	@Transactional
	public String getPid(String pid) {
		if (null == pid || "".equals(pid)) {
			pid = assetPoolDao.getLimitOne().getOid();
		}
		
		return pid;
	}
	
	/**
	 * 编辑资产池
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void editPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = assetPoolDao.findOne(form.getOid());
		entity.setName(form.getName());
		entity.setScale(form.getScale());
		entity.setCashRate(form.getCashRate());
		entity.setCashtoolRate(form.getCashtoolRate());
		entity.setTargetRate(form.getTargetRate());
		entity.setState(AssetPoolEntity.PoolState.get("ASSETPOOLSTATE_01"));
		entity.setOperator(uid);
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		assetPoolDao.save(entity);
		
		for (String s : form.getScopes()) {
			scopeService.save(entity, s);
		}
	}
	
	/**
	 * 编辑资产池账户信息
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void editPoolForCash(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = assetPoolDao.findOne(form.getOid());
		// 原规模
		BigDecimal scale = entity.getScale();
		// 当前规模
		BigDecimal nscale = scale.subtract(entity.getCashPosition())
				.add(form.getCashPosition()).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal cashRate = form.getCashPosition().divide(nscale, 4, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal cashtoolRate = entity.getCashtoolFactRate().multiply(scale)
				.divide(nscale, 4, BigDecimal.ROUND_HALF_UP).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal targetRate = entity.getTargetFactRate().multiply(scale)
				.divide(nscale, 4, BigDecimal.ROUND_HALF_UP).setScale(4, BigDecimal.ROUND_HALF_UP);
		entity.setScale(nscale);
		entity.setCashPosition(form.getCashPosition());
		entity.setCashFactRate(cashRate);
		entity.setCashtoolFactRate(cashtoolRate);
		entity.setTargetFactRate(targetRate);
		entity.setOperator(uid);
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		assetPoolDao.save(entity);
	}
	
	/**
	 * 逻辑删除资产池
	 * @param pid
	 */
	@Transactional
	public void updateAssetPool(String pid) {
		assetPoolDao.updateAssetPool(pid);
	}

	/**
	 * 获取所有资产池列表
	 * @param name
	 * @return
	 */
	@Transactional
	public List<AssetPoolForm> getListByName(String name) {
		List<AssetPoolForm> formList = Lists.newArrayList();
		List<AssetPoolEntity> entityList = assetPoolDao.getListByName(name);
		if (!entityList.isEmpty()) {
			AssetPoolForm form = null;
			for (AssetPoolEntity entity : entityList) {
				form = new AssetPoolForm();
				try {
					BeanUtils.copyProperties(form, entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				formList.add(form);
			}
		}
		
		return formList;
	}
	
	/**
	 * 定时初始化资产池的每日收益计算和收益分配的状态
	 * @return
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void updateState() {
		// 所有资产池列表
		List<AssetPoolEntity> poolList = assetPoolDao.getListByState();
		if (null != poolList && !poolList.isEmpty()) {
			for (AssetPoolEntity entity : poolList) {
				entity.setScheduleState(AssetPoolEntity.schedule_wjs);
				entity.setIncomeState(AssetPoolEntity.income_wfp);
				entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			}
			assetPoolDao.save(poolList);
		}
	}

	/**
	 * 计算资产池当日的确认收益
	 * @return
	 */
	@Scheduled(cron = "0 0/30 9 * * ?")
	@Transactional
	public void calcPoolProfit() {
		// 所有资产池列表
		List<AssetPoolEntity> poolList = assetPoolDao.getListByState();
		if (null != poolList && !poolList.isEmpty()) {
			// 日期
			Date sqlDate = new Date(System.currentTimeMillis());
			// 统计一共有多少个标的数据
			int jobCount = 0;
			// 统计一共执行了多少个标的数据
			int successCount = 0;
			ScheduleLog log = new ScheduleLog();
			log.setOid(StringUtil.uuid());
			log.setBaseDate(sqlDate);
			log.setStartTime(new Timestamp(System.currentTimeMillis()));
			
			for (AssetPoolEntity entity : poolList) {
				this.calcPoolProfit(entity, sqlDate, AssetPoolCalc.EventType.SCHEDULE.toString(),
						jobCount, successCount);
			}
			
			log.setJobCount(jobCount);
			log.setSuccessCount(successCount);
			log.setEndTime(new Timestamp(System.currentTimeMillis()));
			logService.save(log);
		}
	}

	/**
	 * 计算资产池当日的确认收益--手动触发
	 * @return
	 */
	@Transactional
	public void userPoolProfit(String oid, String operator, String type) {
		// 资产池
		AssetPoolEntity entity = assetPoolDao.findOne(oid);
		entity.setOperator(operator);
		// 日期
		Date sqlDate = new Date(System.currentTimeMillis());
		// 统计一共有多少个标的数据
		int jobCount = 0;
		// 统计一共执行了多少个标的数据
		int successCount = 0;
		ScheduleLog log = new ScheduleLog();
		log.setOid(StringUtil.uuid());
		log.setBaseDate(sqlDate);
		log.setStartTime(new Timestamp(System.currentTimeMillis()));
		
		this.calcPoolProfit(entity, sqlDate, type,
				jobCount, successCount);
		
		log.setJobCount(jobCount);
		log.setSuccessCount(successCount);
		log.setEndTime(new Timestamp(System.currentTimeMillis()));
		logService.save(log);
	}
	
	/**
	 * 计算资产池当日的确认收益
	 * @param assetPool
	 * @param fundCalcList
	 * @param trustCalcList
	 * @param poolCalcList
	 * @return
	 */
	@Transactional
	public void calcPoolProfit(AssetPoolEntity assetPool, Date baseDate, 
			String type, int jobCount, int successCount) {
		// 总收益
		BigDecimal totalProfit = BigDecimal.ZERO;
		// 会计分录
//		List<Income> incomeList = Lists.newArrayList();
		// 记录有问题的标的数据oid
		List<ErrorCalc> errorList = Lists.newArrayList();
		// 存档错误数据
		ErrorCalc errorCalc = null;
		// 错误信息
		JSONObject jsonObj = null;
		// 参与计算的现金管理工具列表
		List<FundEntity> fcList = Lists.newArrayList();
		// 参与计算的募集期信托标的列表
		List<TrustEntity> mjq_tcList = Lists.newArrayList();
		// 参与计算的存续期信托标的列表
		List<TrustEntity> cxq_tcList = Lists.newArrayList();
		// 计算结果
		// 现金管理工具每日定时收益数据
		List<FundCalc> fundCalcList = Lists.newArrayList();
		// 信托标的每日定时收益数据
		List<TrustCalc> trustCalcList = Lists.newArrayList();
		
		try {
			// 持仓的现金管理工具
			List<FundEntity> fundList = fundService.findFundListByPid(assetPool.getOid());
			if (null != fundList && !fundList.isEmpty()) {
				jobCount += fundList.size();
				for (FundEntity entity : fundList) {
					if (null == entity.getCashTool().getDailyProfit()) {
						errorCalc = new ErrorCalc();
						jsonObj = new JSONObject();
						errorCalc.setOid(StringUtil.uuid());
						errorCalc.setCashTool(entity.getCashTool());
						errorCalc.setAssetPool(assetPool);
						jsonObj.put("DaylyProfit", "收益率为空");
						errorCalc.setMessage(jsonObj);
						errorCalc.setOperator(assetPool.getOperator());
						errorCalc.setCreateTime(new Timestamp(System.currentTimeMillis()));
						errorList.add(errorCalc);
					} else {
						fcList.add(entity);
					}
				}
			}
			
			// 持仓的货基标的
			List<TrustEntity> trustList = trustService.findTargetListByPid(assetPool.getOid());
			if (null != trustList && !trustList.isEmpty()) {
				jobCount += trustList.size();
				for (TrustEntity entity : trustList) {
					// 判断是否成立
					if ("STAND_UP".equals(entity.getTarget().getLifeState())) {
						// 判断收益方式（amortized_cost：摊余成本法；book_value：账面价值法）
						if ("amortized_cost".equals(entity.getProfitType())) {
							if (null == entity.getTarget().getExpAror()
									|| null == entity.getTarget().getContractDays()) {
								errorCalc = new ErrorCalc();
								jsonObj = new JSONObject();
								errorCalc.setOid(StringUtil.uuid());
								errorCalc.setTarget(entity.getTarget());
								errorCalc.setAssetPool(assetPool);
								if (null == entity.getTarget().getExpAror())
									jsonObj.put("ExpAror", "收益率为空");
								if (null == entity.getTarget().getContractDays())
									jsonObj.put("ContractDays", "合同年天数为空");
								errorCalc.setMessage(jsonObj);
								errorCalc.setOperator(assetPool.getOperator());
								errorCalc.setCreateTime(new Timestamp(System.currentTimeMillis()));
								errorList.add(errorCalc);
							} else {
								cxq_tcList.add(entity);
							}
						}
					} else {
						// 判断是否在募集期
						if (!DateUtil.compare_current(entity.getTarget().getCollectEndDate())) {
							if (null == entity.getTarget().getCollectIncomeRate()
									|| null == entity.getTarget().getContractDays()) {
								errorCalc = new ErrorCalc();
								jsonObj = new JSONObject();
								errorCalc.setOid(StringUtil.uuid());
								errorCalc.setTarget(entity.getTarget());
								errorCalc.setAssetPool(assetPool);
								if (null == entity.getTarget().getCollectIncomeRate())
									jsonObj.put("CollectIncomeRate", "募集期收益率为空");
								if (null == entity.getTarget().getContractDays())
									jsonObj.put("ContractDays", "合同年天数为空");
								errorCalc.setMessage(jsonObj);
								errorCalc.setOperator(assetPool.getOperator());
								errorCalc.setCreateTime(new Timestamp(System.currentTimeMillis()));
								errorList.add(errorCalc);
							} else {
								mjq_tcList.add(entity);
							}
						}
					}
				}
			}
			if (type.equals(AssetPoolCalc.EventType.SCHEDULE.toString())) {
				if (errorList.size() > 0) {
					assetPool.setScheduleState(AssetPoolEntity.schedule_wjs);
//					throw new RuntimeException("=================定时任务未执行，待数据补录==============="); 
				}
			} else if (type.equals(AssetPoolCalc.EventType.USER_CALC.toString())) {
				AssetPoolCalc calc = new AssetPoolCalc();
				calc.setOid(StringUtil.uuid());
				calc.setAssetPool(assetPool);
				
				totalProfit = this.calcFundProfit(calc, baseDate, totalProfit, fcList, fundCalcList);
				totalProfit = this.calcTrustProfitForCollect(calc, baseDate, totalProfit, mjq_tcList, trustCalcList);
				totalProfit = this.calcTrustProfitForDuration(calc, baseDate, totalProfit, cxq_tcList, trustCalcList);
				
				calc.setCapital(assetPool.getScale().add(totalProfit).setScale(4, BigDecimal.ROUND_HALF_UP));
				calc.setProfit(totalProfit);
				calc.setBaseDate(baseDate);
				calc.setEventType(AssetPoolCalc.EventType.USER_CALC);
				calc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				
				poolCalcService.saveOne(calc);
				if (fundCalcList.size() > 0)
					fundCalcService.save(fundCalcList);
				if (trustCalcList.size() > 0)
					trustCalcService.save(trustCalcList);
				
				successCount = fundCalcList.size() + trustCalcList.size();
				
				assetPool.setConfirmProfit(totalProfit);
				assetPool.setFactProfit(totalProfit);
				assetPool.setScheduleState(AssetPoolEntity.schedule_bfjs);
				assetPool.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			} else {
				AssetPoolCalc calc = new AssetPoolCalc();
				calc.setOid(StringUtil.uuid());
				calc.setAssetPool(assetPool);
				calc.setBaseDate(baseDate);
				calc.setEventType(AssetPoolCalc.EventType.USER_NONE);
				assetPool.setScheduleState(AssetPoolEntity.schedule_drbjs);
				assetPool.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				poolCalcService.saveOne(calc);
				
				successCount = 1;
				
//				throw new RuntimeException("=================定时任务默认录入一条初始化数据==============="); 
			}
			assetPoolDao.save(assetPool);
			
			if (errorList.size() > 0) {
				errorCalcService.save(errorList);
			}
			
//			if (incomeList.size() > 0)
//				incomeService.incomeConfirm(assetPool.getOid(), incomeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算现金管理工具每日收益
	 * @param assetPoolCalc
	 * @param baseDate
	 * @param totalProfit
	 * @param fundList
	 * @param fundCalcList
	 * @param incomeList
	 */
	@Transactional
	public BigDecimal calcFundProfit(AssetPoolCalc assetPoolCalc, Date baseDate,
			BigDecimal totalProfit,
			List<FundEntity> fundList,
			List<FundCalc> fundCalcList) {
		// 当日收益
		BigDecimal dayProfit = BigDecimal.ZERO;
		if (null != fundList && !fundList.isEmpty()) {
			for (FundEntity entity : fundList) {
				dayProfit = entity.getAmount().multiply(
						entity.getCashTool().getDailyProfit()
						.divide(new BigDecimal(10000)))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				entity.setAmount(entity.getAmount().add(dayProfit)
						.setScale(4, BigDecimal.ROUND_HALF_UP));
				entity.setDailyProfit(dayProfit);
				entity.setTotalProfit(entity.getTotalProfit().add(dayProfit)
						.setScale(4, BigDecimal.ROUND_HALF_UP));

				FundCalc calc = new FundCalc();
				calc.setOid(StringUtil.uuid());
				calc.setCashTool(entity.getCashTool());
				calc.setAssetPoolCalc(assetPoolCalc);
				calc.setCapital(entity.getAmount());
				calc.setInterest(calc.getInterest().add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP));
				calc.setYield(entity.getCashTool().getDailyProfit());
				calc.setIncome(dayProfit);
				calc.setBaseDate(baseDate);
				calc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				fundCalcList.add(calc);
				
				totalProfit = totalProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
				
//				Income income = new Income(calc.getOid(), dayProfit, IncomeType.CASHTOOL);
//				incomeList.add(income);
			}
			fundService.save(fundList);
		}
		
		return totalProfit;
	}
	
	/**
	 * 计算募集期的信托标的每日收益
	 * @param assetPoolCalc
	 * @param baseDate
	 * @param totalProfit
	 * @param trustList
	 * @param trustCalcList
	 * @param incomeList
	 */
	@Transactional
	public BigDecimal calcTrustProfitForCollect(AssetPoolCalc assetPoolCalc, Date baseDate,
			BigDecimal totalProfit, 
			List<TrustEntity> trustList, 
			List<TrustCalc> trustCalcList) {
		// 当日收益
		BigDecimal dayProfit = BigDecimal.ZERO;
		if (null != trustList && !trustList.isEmpty()) {
			for (TrustEntity entity : trustList) {
				dayProfit = entity.getInvestVolume()
						.multiply(entity.getTarget().getCollectIncomeRate())
						.divide(new BigDecimal(entity.getTarget().getContractDays()), 4, BigDecimal.ROUND_HALF_UP)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				entity.setDailyProfit(dayProfit);
				entity.setTotalProfit(entity.getTotalProfit().add(dayProfit)
						.setScale(4, BigDecimal.ROUND_HALF_UP));
				
				TrustCalc calc = new TrustCalc();
				calc.setOid(StringUtil.uuid());
				calc.setTarget(entity.getTarget());
				calc.setAssetPool(assetPoolCalc);
				calc.setCapital(entity.getInvestVolume());
				calc.setYield(entity.getTarget().getCollectIncomeRate());
				calc.setProfit(dayProfit);
				calc.setBaseDate(baseDate);
				calc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				trustCalcList.add(calc);
				
				totalProfit = totalProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
				
//				Income income = new Income(calc.getOid(), dayProfit, IncomeType.TARGET);
//				incomeList.add(income);
			}
			trustService.save(trustList);
		}
		
		return totalProfit;
	}
	
	/**
	 * 计算存续期的信托标的给每日收益
	 * @param assetPoolCalc
	 * @param baseDate
	 * @param totalProfit
	 * @param trustList
	 * @param trustCalcList
	 * @param incomeList
	 */
	@Transactional
	public BigDecimal calcTrustProfitForDuration(AssetPoolCalc assetPoolCalc, Date baseDate, 
			BigDecimal totalProfit,
			List<TrustEntity> trustList, 
			List<TrustCalc> trustCalcList) {
		// 当日收益
		BigDecimal dayProfit = BigDecimal.ZERO;
		if (null != trustList && !trustList.isEmpty()) {
			for (TrustEntity entity : trustList) {
				dayProfit = entity.getInvestVolume()
						.multiply(entity.getTarget().getExpAror())
						.divide(new BigDecimal(entity.getTarget().getContractDays()), 4, BigDecimal.ROUND_HALF_UP)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				entity.setDailyProfit(dayProfit);
				entity.setTotalProfit(entity.getTotalProfit().add(dayProfit)
						.setScale(4, BigDecimal.ROUND_HALF_UP));
				
				TrustCalc calc = new TrustCalc();
				calc.setOid(StringUtil.uuid());
				calc.setTarget(entity.getTarget());
				calc.setAssetPool(assetPoolCalc);
				calc.setCapital(entity.getInvestVolume());
				calc.setYield(entity.getTarget().getExpAror());
				calc.setProfit(dayProfit);
				calc.setBaseDate(baseDate);
				calc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				trustCalcList.add(calc);
				
				totalProfit = totalProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
				
//				Income income = new Income(calc.getOid(), dayProfit, IncomeType.TARGET);
//				incomeList.add(income);
			}
			trustService.save(trustList);
		}
		
		return totalProfit;
	}
	
	/**
	 * 更新资产池的偏离损益
	 * @param form
	 */
	@Transactional
	public void updateDeviationValue(AssetPoolForm form, String operator) {
		AssetPoolEntity entity = assetPoolDao.findOne(form.getOid());
		BigDecimal marketValue = form.getShares().multiply(form.getNav())
				.setScale(4, BigDecimal.ROUND_HALF_UP);
		entity.setMarketValue(marketValue);
		entity.setDeviationValue(marketValue.subtract(entity.getScale())
				.setScale(4, BigDecimal.ROUND_HALF_UP));
		entity.setOperator(operator);
		assetPoolDao.save(entity);
//		BigDecimal nscale = entity.getScale().add(
//				form.getDeviationValue().subtract(entity.getDeviationValue())
//				.setScale(4, BigDecimal.ROUND_HALF_UP));
//
//		this.calcAssetPoolRate(entity, nscale, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	/**
	 * 重计算资产池的投资占比
	 * @param entity
	 * @param nscale
	 * 				当前资产池估值
	 * @param fvalue
	 * 				现金管理工具纠偏的差额
	 * @param tvalue
	 * 				投资标的纠偏的差额
	 */
	public void calcAssetPoolRate(AssetPoolEntity entity, BigDecimal nscale, BigDecimal fvalue, BigDecimal tvalue) {
		// 原规模
		BigDecimal scale = entity.getScale();
		BigDecimal cashRate = entity.getCashPosition().divide(nscale, 4, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal cashtoolRate = (entity.getCashtoolFactRate().multiply(scale).add(fvalue))
				.divide(nscale, 4, BigDecimal.ROUND_HALF_UP);
		BigDecimal targetRate = (entity.getTargetFactRate().multiply(scale).add(tvalue))
				.divide(nscale, 4, BigDecimal.ROUND_HALF_UP);
		entity.setScale(nscale);
		entity.setCashFactRate(cashRate);
		entity.setCashtoolFactRate(cashtoolRate);
		entity.setTargetFactRate(targetRate);
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		assetPoolDao.save(entity);
	}
}
