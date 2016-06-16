package com.guohuai.asset.manage.boot.duration.fact.calibration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void save(MarketAdjust entity) {
		adjustDao.save(entity);
	}
	
	public void save(List<MarketAdjustOrder> list) {
		adjustOrderDao.save(list);
	}
	
	public List<MarketAdjust> getMarketAdust() {
		List<MarketAdjust> list = adjustDao.findAll();
		
		return list;
	}
}
