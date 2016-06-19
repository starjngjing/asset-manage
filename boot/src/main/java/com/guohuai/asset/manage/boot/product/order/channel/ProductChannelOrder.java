package com.guohuai.asset.manage.boot.product.order.channel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.channel.Channel;
import com.guohuai.asset.manage.boot.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_PRODUCT_CHANNEL_ORDER")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChannelOrder implements Serializable {

	private static final long serialVersionUID = -8728888636245221185L;

	@Id
	private String oid;
	@ManyToOne
	@JoinColumn(name = "productOid")
	private Product product;// 产品
	@ManyToOne
	@JoinColumn(name = "channelOid")
	private Channel channel; // 渠道
	private String creator;
	private Timestamp createTime;
	private String auditor;
	private Timestamp auditTime;
	private String status;

}
