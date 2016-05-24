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
		this.id = entity.getOid();
		this.text = entity.getTitle();
		this.state = entity.getState();
		this.remark = entity.getCheckDesc();
		this.file = entity.getCheckFile();
	}

	private String id;
	private String text;
	private String state;
	private String remark;
	private String file;

}
