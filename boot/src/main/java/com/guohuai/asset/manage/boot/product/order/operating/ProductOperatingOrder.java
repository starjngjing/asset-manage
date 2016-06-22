package com.guohuai.asset.manage.boot.product.order.operating;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_PRODUCT_OPERATING_ORDER")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOperatingOrder {

	// 开启申购 PURCHASE_ON
	public static final String TYPE_PURCHASE_ON = "PURCHASE_ON";
	// 关闭申购 PURCHASE_OFF
	public static final String TYPE_PURCHASE_OFF = "PURCHASE_OFF";
	// 开启赎回 REDEEM_ON
	public static final String TYPE_REDEEM_ON = "REDEEM_ON";
	// 关闭赎回 REDEEM_OFF
	public static final String TYPE_REDEEM_OFF = "REDEEM_OFF";
	
	// 待审核
	public static final String STATUS_SUBMIT = "SUBMIT";
	//审核通过
	public static final String STATUS_PASS = "PASS";
	//审核驳回
	public static final String STATUS_FAIL = "FAIL";
	//删除
	public static final String STATUS_DELETE = "DELETE";

	@Id
	private String oid;
	@ManyToOne
	@JoinColumn(name = "productOid")
	private Product product;
	private String type;
	private String creator;
	private Timestamp createTime;
	private String auditor;
	private Timestamp auditTime;
	private String status;
}
