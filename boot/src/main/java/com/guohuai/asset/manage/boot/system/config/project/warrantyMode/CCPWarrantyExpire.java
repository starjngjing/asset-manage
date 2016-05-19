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
@Table(name = "T_GAM_CCP_WARRANTY_EXPIRE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCPWarrantyExpire implements Serializable {

	private static final long serialVersionUID = -4004355457279663221L;

	@Id
	private String oid;
	private String title;
	private double weight;

}
