package com.guohuai.asset.manage.boot.dict;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_DICT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dict {

	@Id
	private String oid;
	private String cate;
	private String name;
	private String rank;
	private String poid;

	@Transient
	private List<Dict> subs = new ArrayList<Dict>();

}
