package com.guohuai.asset.manage.boot.Duration.fund;

import javax.persistence.Id;

import lombok.Data;

/**
 * 存续期--账户对象
 * @author star.zhu
 * 2016年5月16日
 */
@Data
public class AccountEntity {

	@Id
	private String id;
}
