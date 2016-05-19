package com.guohuai.asset.manage.boot.system.config.project.warrantyMode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CCPWarrantyModeForm {

	private String oid;
	@Enumerations(values = { "GUARANTEE", "MORTGAGE", "HYPOTHECATION" })
	private String type;
	@NotEmpty
	@NotNull
	@NotBlank
	private String title;
	@Digits(integer = 4, fraction = 4)
	private double weight100;

}
