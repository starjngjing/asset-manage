package com.guohuai.asset.manage.boot.product.order.salePosition;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "T_GAM_PRODUCT_SALE_POSITION_ORDER")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSalePositionOrder implements Serializable {

	private static final long serialVersionUID = 4839202649426179728L;

	@Id
	private String oid;
	@ManyToOne
	@JoinColumn(name = "productOid")
	private Product product;
	private BigDecimal volume;
	private String creator;
	private Timestamp createTime;
	private String auditor;
	private Timestamp auditTime;
	private String status;

}
