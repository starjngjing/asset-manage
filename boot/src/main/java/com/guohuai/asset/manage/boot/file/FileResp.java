package com.guohuai.asset.manage.boot.file;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResp {

	public FileResp(SaveFileForm form) {
		super();
		this.oid = form.getOid();
		this.name = form.getName();
		this.furl = form.getFurl();
		this.size = form.getSize();
		this.sizeh = this.calcSizeh(form.getSize());
	}

	private String oid;
	private String name;
	private String furl;
	private long size;
	private String sizeh;
	private String fversion;

	private String calcSizeh(long size) {
		if (size < 1024L) {
			// 不到1K的, 按字节
			return size + "B";
		} else if (size >= 1024L && size < 1048576L) {
			// 超过1K, 不到1M的, 转化为K, 保留2位小数
			return new BigDecimal(size).divide(new BigDecimal(1024L), 2, RoundingMode.HALF_UP).toString() + "K";
		} else if (size >= 1048576L && size < 1073741824L) {
			// 超过1M, 不到1G的, 转化为M, 保留2位小数
			return new BigDecimal(size).divide(new BigDecimal(1048576L), 2, RoundingMode.HALF_UP).toString() + "M";
		} else {
			// 超过1G的, 按G转化
			return new BigDecimal(size).divide(new BigDecimal(1073741824L), 2, RoundingMode.HALF_UP).toString() + "G";
		}
	}

	public FileResp(File f) {
		// TODO Auto-generated constructor stub
	}

}
