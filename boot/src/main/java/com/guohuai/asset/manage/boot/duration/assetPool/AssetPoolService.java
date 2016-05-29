package com.guohuai.asset.manage.boot.duration.assetPool;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
import com.guohuai.asset.manage.boot.duration.assetPool.scope.ScopeService;
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
	
	/**
	 * 新建资产池
	 * @param form
	 * @param uid
	 */
	@Transactional
	public void createPool(AssetPoolForm form, String uid) {
		AssetPoolEntity entity = new AssetPoolEntity();
		try {
			BeanUtils.copyProperties(entity, form);
			entity.setOid(StringUtil.uuid());
			entity.setCashPosition(form.getScale());
			entity.setCashFactRate(new BigDecimal(100));
			entity.setCashtoolFactRate(BigDecimal.ZERO);
			entity.setTargetFactRate(BigDecimal.ZERO);
			entity.setFreezeCash(BigDecimal.ZERO);
			entity.setTransitCash(BigDecimal.ZERO);
			entity.setConfirmProfit(BigDecimal.ZERO);
			entity.setFactProfit(BigDecimal.ZERO);
			entity.setState("未审核");
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
			entity.setState("成立");
		} else {
			entity.setState("未通过");
		}
	}

	/**
	 * 获取所有资产池列表
	 * @return
	 */
	@Transactional
	public List<AssetPoolForm> getAllList(Specification<AssetPoolEntity> spec, Pageable pageable) {
		List<AssetPoolForm> formList = Lists.newArrayList();
		Page<AssetPoolEntity> entityList = assetPoolDao.findAll(spec, pageable);
		AssetPoolForm form = null;
		if (null != entityList.getContent() && entityList.getContent().size() > 0) {
			for (AssetPoolEntity entity : entityList.getContent()) {
				form = new AssetPoolForm();
				try {
					BeanUtils.copyProperties(form, entity);
					form.setState(entity.getState().equals("未审核") ?  "0" : entity.getState().equals("成立") ? "1" : "-1");
				} catch (Exception e) {
					e.printStackTrace();
				}
				formList.add(form);
			}
		}
		
		return formList;
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
		
		return form;
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
		entity.setCashPosition(form.getCashPosition());
		entity.setState("未审核");
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
		BigDecimal nscale = entity.getScale().subtract(entity.getCashPosition()
				.add(form.getCashPosition())).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal cashRate = form.getCashPosition().divide(nscale)
				.multiply(new BigDecimal(100)).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal cashtoolRate = entity.getCashtoolFactRate().multiply(scale)
				.divide(nscale).setScale(4, BigDecimal.ROUND_HALF_UP);
		BigDecimal targetRate = entity.getTargetFactRate().multiply(scale)
				.divide(nscale).setScale(4, BigDecimal.ROUND_HALF_UP);
		entity.setName(form.getName());
		entity.setScale(form.getScale());
		entity.setCashRate(form.getCashRate());
		entity.setCashtoolRate(form.getCashtoolRate());
		entity.setTargetRate(form.getTargetRate());
		entity.setCashPosition(form.getCashPosition());
		entity.setCashFactRate(cashRate);
		entity.setCashtoolFactRate(cashtoolRate);
		entity.setTargetFactRate(targetRate);
		entity.setState("成立");
		entity.setOperator(uid);
		entity.setUpdateTime(DateUtil.getSqlCurrentDate());
		
		assetPoolDao.save(entity);
		
		for (String s : form.getScopes()) {
			scopeService.save(entity, s);
		}
		
		assetPoolDao.save(entity);
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
	 * 计算资产池当日的确认收益
	 * @return
	 */
	@Scheduled(cron = "0 15 12 * * ?")
	@Transactional
	public void calcPoolProfit() {
		// 所有资产池列表
		List<AssetPoolEntity> poolList = assetPoolDao.findAll();
		if (null != poolList && !poolList.isEmpty()) {
			for (AssetPoolEntity entity : poolList) {
				poolList.add(this.calcPoolProfit(entity));
			}
			
			assetPoolDao.save(poolList);
		}
		
	}
	
	/**
	 * 计算资产池当日的确认收益
	 * @param assetPool
	 * @return
	 */
	@Transactional
	public AssetPoolEntity calcPoolProfit(AssetPoolEntity assetPool) {
		// 确认收益
		BigDecimal confirmProfit = BigDecimal.ZERO;
		// 当日收益
		BigDecimal dayProfit = BigDecimal.ZERO;
		
		// 持仓的现金管理工具
		List<FundEntity> fundList = fundService.findFundListByPid(assetPool.getOid());
		if (null != fundList && !fundList.isEmpty()) {
			for (FundEntity entity : fundList) {
				dayProfit = entity.getAmount().multiply(
						entity.getCashTool().getDailyProfit()
						.divide(new BigDecimal(10000)))
						.setScale(4, BigDecimal.ROUND_HALF_UP);
				confirmProfit = confirmProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
				entity.setAmount(entity.getAmount().add(dayProfit)
						.setScale(4, BigDecimal.ROUND_HALF_UP));
			}
		}
		
		// 持仓的货基标的
		List<TrustEntity> trustList = trustService.findTargetListByPid(assetPool.getOid());
		if (null != trustList && !trustList.isEmpty()) {
			for (TrustEntity entity : trustList) {
				// 判断是否成立
				if ("STAND_UP".equals(entity.getTarget().getLifeState())) {
					// 判断收益方式（amortized_cost：摊余成本法；book_value：账面价值法）
					if ("amortized_cost".equals(entity.getProfitType())) {
						dayProfit = entity.getInvestAmount()
								.multiply(entity.getTarget().getExpAror())
								.divide(new BigDecimal(entity.getTarget().getContractDays()))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						confirmProfit = confirmProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
					}
				} else {
					// 判断是否在募集期
					if (!DateUtil.compare_current(entity.getTarget().getCollectEndDate())) {
						dayProfit = entity.getInvestAmount()
								.multiply(entity.getTarget().getCollectIncomeRate())
								.divide(new BigDecimal(entity.getTarget().getContractDays()))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
						confirmProfit = confirmProfit.add(dayProfit).setScale(4, BigDecimal.ROUND_HALF_UP);
					}
				}
			}
		}
		
		assetPool.setConfirmProfit(confirmProfit);
		assetPool.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		return assetPool;
	}
}
