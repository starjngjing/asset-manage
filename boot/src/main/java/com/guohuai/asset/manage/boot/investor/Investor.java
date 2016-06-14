package com.guohuai.asset.manage.boot.investor;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 持有人表
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INVESTOR")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investor implements Serializable {

	private static final long serialVersionUID = 766183109221377996L;
	
	/**
	 * 类型type：
	 */
	public static final String TYPE_Spv = "SPV";//SPV
	public static final String TYPE_Investor= "INVESTOR";//零售投资人
	
	
	@Id
	private String oid;//序号
	private String sn;//持有人标识
	private String phoneNum;//手机号	
	private String realName;//真实姓名
	private String type;//类型
	private Timestamp createTime;
	private Timestamp updateTime;
	

}
