package com.guohuai.asset.manage.boot.product.productChannel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guohuai.asset.manage.boot.channel.Channel;
import com.guohuai.asset.manage.boot.product.Product;
import com.guohuai.asset.manage.boot.product.order.channel.ProductChannelOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_PRODUCT_CHANNEL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChannel implements Serializable {

	private static final long serialVersionUID = -6728951899544117199L;
	
	public static final String MARKET_STATE_Noshelf = "NOSHELF";//待上架
	public static final String MARKET_STATE_Shelfing = "SHELFING";//上架中
	public static final String MARKET_STATE_Onshelf = "ONSHELF";//已上架
	public static final String MARKET_STATE_Offshelf = "OFFSHELF";//已下架
	
	@Id
	private String oid;
	@ManyToOne
	@JoinColumn(name="productOid")
	private Product product;//产品
	@ManyToOne
	@JoinColumn(name="channelOid")
	private Channel  channel;//渠道
	@ManyToOne
	@JoinColumn(name="orderOid")
	private ProductChannelOrder order;
	private String operator;//申请人
	private Timestamp updateTime;
	private Timestamp createTime;//申请渠道销售时间
	private String marketState;//上下架状态
	private Timestamp rackTime;//上架时间
	private Timestamp downTime;//下架时间	
	
}
