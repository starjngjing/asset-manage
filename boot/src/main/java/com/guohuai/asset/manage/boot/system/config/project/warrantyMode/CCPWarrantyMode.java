package com.guohuai.asset.manage.boot.system.config.project.warrantyMode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_CCP_WARRANTY_MODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCPWarrantyMode implements Serializable {

	private static final long serialVersionUID = -3619709857554967150L;

	@Id
	private String oid;
	private String type;
	private String title;
	private double weight;

}
