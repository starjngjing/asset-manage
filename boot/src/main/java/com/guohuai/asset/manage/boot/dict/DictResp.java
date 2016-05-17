package com.guohuai.asset.manage.boot.dict;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictResp {

	public DictResp(Dict dict) {
		this.oid = dict.getOid();
		this.cate = dict.getCate();
		this.name = dict.getName();
		this.rank = dict.getRank();
		this.poid = dict.getPoid();
		this.subs = new ArrayList<DictResp>();
	}

	private String oid;
	private String cate;
	private String name;
	private String rank;
	private String poid;

	private List<DictResp> subs;

}
