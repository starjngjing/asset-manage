package com.guohuai.asset.manage.boot.Duration.fund;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 存续期--账户对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
@Entity
@Table(name = "T_GAM_ASSETPOOL_CASH_LOG")
public class AccountEntity {

	@Id
	private String id;
}
