package com.guohuai.asset.manage.boot.system.config.project.warrantor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CCPWarrantorForm {

	private String oid;
	@NotNull
	@NotBlank
	@NotEmpty
	private String title;
	@Digits(integer = 4, fraction = 0)
	private int lowScore;
	@Digits(integer = 4, fraction = 0)
	private int highScore;
	@Digits(integer = 4, fraction = 4)
	private double weight;

}
