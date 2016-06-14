package com.guohuai.asset.manage.boot.investor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestorDao extends JpaRepository<Investor, String>, JpaSpecificationExecutor<Investor> {
	
	/**
	 * 根据持有人标识获取持有人实体
	 * @param sn
	 * @return {@link Investor}
	 */
	public Investor findBySn(String sn);
	
	/**
	 * 根据持有人类型获取持有人实体
	 * @param type
	 * @return {@link Investor}
	 */
	public Investor findByType(String type);

}
