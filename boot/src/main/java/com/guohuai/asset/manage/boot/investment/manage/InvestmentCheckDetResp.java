package com.guohuai.asset.manage.boot.investment.manage;

import com.guohuai.asset.manage.boot.investment.InvestmentMeetingCheck;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 检查项列表详情返回域
 * 
 * @author lirong
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InvestmentCheckDetResp {

	public InvestmentCheckDetResp(InvestmentMeetingCheck entity) {
		this.oid = entity.getOid();
		this.title = entity.getTitle();
		this.state = entity.getState();
		this.checkDesc = entity.getCheckDesc();
		this.checkFile = entity.getCheckFile();
	}

	private String oid;
	private String title;
	private String state;
	private String checkDesc;
	private String checkFile;

}
