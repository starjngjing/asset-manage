package com.guohuai.asset.manage.boot.duration.fact.calibration;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.guohuai.asset.manage.boot.acct.books.document.SPVDocumentService;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolService;
import com.guohuai.asset.manage.boot.order.SpvOrderService;
import com.guohuai.asset.manage.component.util.StringUtil;

/**
 * 市值校准
 * @author star.zhu
 * 2016年6月16日
 */
@Service
public class MarketAdjustService {

	@Autowired
	private MarketAdjustDao adjustDao;
	@Autowired
	private MarketAdjustOrderDao adjustOrderDao;
	
	@Autowired
	private SpvOrderService orderService;
	@Autowired
	private AssetPoolService poolService;
	
	@Autowired
	private SPVDocumentService spvService;
	
	@Transactional
	public void save(MarketAdjustEntity entity) {
		adjustDao.save(entity);
	}
	
	@Transactional
	public void save(List<MarketAdjustOrderEntity> list) {
		adjustOrderDao.save(list);
	}
	
	@Transactional
	public MarketAdjustEntity findOne(String oid) {
		return adjustDao.findOne(oid);
	}
	
	/**
	 * 录入市值校准记录
	 * @param form
	 */
	@Transactional
	public void saveMarketAdjust(MarketAdjustForm form) {
		try {
			MarketAdjustEntity entity = new MarketAdjustEntity();
			BeanUtils.copyProperties(entity, form);
			entity.setOid(StringUtil.uuid());
			entity.setAssetPool(poolService.getByOid(form.getAssetpoolOid()));
			this.save(entity);

			List<MarketAdjustOrderEntity> list = Lists.newArrayList();
			if (null != form.getOids() && !"".equals(form.getOids())) {
				String[] oids = form.getOids().substring(1, form.getOids().length() - 1).split(", ");
				for (int i = 0; i < oids.length; i ++) {
					list.add(this.saveMarketAdjustOrder(entity.getOid(), oids[i]));
				}
				adjustOrderDao.save(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关系数据维护
	 * @param aid
	 * 			市值校准记录订单oid
	 * @param oid
	 * 			销售订单oid
	 * @return
	 */
	@Transactional
	public MarketAdjustOrderEntity saveMarketAdjustOrder(String aid, String oid) {
		MarketAdjustOrderEntity entity = new MarketAdjustOrderEntity();
		entity.setOid(StringUtil.uuid());
		entity.setAdjustOid(aid);
		entity.setOrderOid(oid);
		
		return entity;
	}
	
	public void auditMarketAdjust(String oid, String type, String operator) {
		MarketAdjustEntity market = adjustDao.findOne(oid);
		if ("pass".equals(type)) {
			market.setStatus(MarketAdjustEntity.PASS);
			spvService.incomeConfirm(market.getAssetPool().getOid(), oid, market.getProfit());
		} else
			market.setStatus(MarketAdjustEntity.FAIL);
		market.setAuditor(operator);
		market.setAuditTime(new Timestamp(System.currentTimeMillis()));
		adjustDao.save(market);
	}
	
	/**
	 * 市值校准记录 列表
	 * @param pid
	 * @param pageable
	 * @return
	 */
	@Transactional
	public Page<MarketAdjustEntity> getMarketAdjustList(String pid, Pageable pageable) {
		Page<MarketAdjustEntity> list = adjustDao.getListByPid(pid, pageable);
		
		return list;
	}
	
	/**
	 * 市值校准录入 详情表单
	 * @param oid
	 * @param pid
	 * @return
	 */
	@Transactional
	public MarketAdjustForm getMarketAdjust(String pid) {
		MarketAdjustForm form = new MarketAdjustForm();
		form.setAssetpoolOid(pid);
		Date baseDate = new Date(System.currentTimeMillis());
		List<Object[]> objList = orderService.getListForMarketAdjust(pid, baseDate);
		if (null != objList && !objList.isEmpty()) {
			BigDecimal puchaseAmount = BigDecimal.ZERO;
			BigDecimal redeemAmount = BigDecimal.ZERO;
			// 记录销售订单oid
			List<String> oidList = Lists.newArrayList();
			for (Object[] obj : objList) {
				if ("REDEEM".equals((String)obj[1])) {
					redeemAmount = redeemAmount.add((BigDecimal)obj[2]).setScale(4, BigDecimal.ROUND_HALF_UP);
				} else {
					puchaseAmount = puchaseAmount.add((BigDecimal)obj[2]).setScale(4, BigDecimal.ROUND_HALF_UP);
				}
				oidList.add((String)obj[0]);
			}
			
			MarketAdjustEntity lastMarket = adjustDao.getMaxBaseDate();
			form.setLastBaseDate(lastMarket.getBaseDate());
			form.setLastShares(lastMarket.getLastShares());
			form.setPurchase(puchaseAmount);
			form.setRedemption(redeemAmount);
			form.setLastOrders(puchaseAmount.subtract(redeemAmount).setScale(4, BigDecimal.ROUND_HALF_UP));
			form.setOids(oidList.toString());
		}
		
		return form;
	}
}
