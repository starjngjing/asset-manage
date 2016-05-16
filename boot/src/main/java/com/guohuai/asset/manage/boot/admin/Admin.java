package com.guohuai.asset.manage.boot.admin;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "oid")
@Builder
public class Admin {

	@Id
	private String oid;
	private int publishCount;
	private int matchingCount;
	private int setupCount;
	private String operator;
	private Timestamp updateTime;
	private Timestamp createTime;

}
