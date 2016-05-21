package com.guohuai.asset.manage.boot.file;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.guohuai.asset.manage.component.web.view.Disview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_FILE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
	
	public static final int STATE_Invalid = 0;
	public static final int STATE_Valid = 1;
	
	public static final String CATE_User = "USER";

	@Id
	private String oid;
	@Disview
	private String fkey;
	private String name;
	private String furl;
	private long size;
	private String sizeh;
	@Disview
	private String cate;
	@Disview
	private int state;
	private String fversion;
	@Disview
	private String operator;
	@Disview
	private Timestamp updateTime;
	@Disview
	private Timestamp createTime;

}
