package com.guohuai.asset.manage.boot.system.config.project.warrantor;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_CCP_WARRANTOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCPWarrantor implements Serializable {

	private static final long serialVersionUID = -1196776787579317636L;

	@Id
	private String oid;
	private String title;
	private int lowScore;
	private int highScore;
	private BigDecimal weight;

}
