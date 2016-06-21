package com.guohuai.asset.manage.boot.system.config.project.warrantyLevel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.guohuai.asset.manage.boot.investment.Investment;
import com.guohuai.asset.manage.boot.project.Project;
import com.guohuai.asset.manage.boot.project.Project.ProjectBuilder;
import com.guohuai.asset.manage.component.persist.UUID;
import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CCPWarrantyLevel extends UUID {

	private static final long serialVersionUID = -7004355457279663225L;

	private String wlevel;
	private String name;
	private String coverLow;
	private BigDecimal lowFactor;
	private BigDecimal highFactor;
	private String coverHigh;

}
