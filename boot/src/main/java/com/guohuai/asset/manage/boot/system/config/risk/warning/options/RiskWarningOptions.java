package com.guohuai.asset.manage.boot.system.config.risk.warning.options;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.system.config.risk.warning.RiskWarning;
import com.guohuai.asset.manage.component.persist.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 预警指标配置表
 * <p>Title: RiskWarningOptions.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年6月7日 下午4:31:53
 */
@Entity
@Table(name = "T_GAM_CCR_RISK_WARNING_OPTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class RiskWarningOptions extends UUID {

	private static final long serialVersionUID = 8777749354109943921L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wariningOid", referencedColumnName = "oid")
	private RiskWarning warning;
	private String wlevel;
	private String param0;
	private String param1;
	private String param2;
	private String param3;
	private Integer seq;
}
