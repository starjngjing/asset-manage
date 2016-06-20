package com.guohuai.asset.manage.boot.system.config.project.warrantyLevel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 风险等级配置
 * 
 * @author vania
 *
 */

@Entity
@Table(name = "T_GAM_CCP_WARRANTY_LEVEL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCPWarrantyLevel implements Serializable {

	private static final long serialVersionUID = -7004355457279663226L;

	@Id
	private String oid;
	private String name;
	private String coverLow;
	private BigDecimal lowFactor;
	private BigDecimal highFactor;
	private String coverHigh;

}