package com.guohuai.asset.manage.boot.order;

import java.math.BigDecimal;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.web.view.BaseResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class SpvOrderResp extends BaseResp {
	
	public SpvOrderResp(InvestorOrder io) {
		this.oid = io.getOid();
		this.orderCode = io.getOrderCode();//订单号
		this.orderType = io.getOrderType();//交易类型
		this.orderCate = io.getOrderCate();//订单类型
		this.orderAmount = io.getOrderAmount();//订单金额	
		this.orderVolume = io.getOrderVolume();//订单份额
		this.orderStatus = io.getOrderStatus();//订单状态
		this.entryStatus = io.getEntryStatus();//订单入账状态
		this.creater = io.getCreater();//订单创建人
		this.createTime = DateUtil.formatDatetime(io.getCreateTime().getTime());//订单创建时间
		this.auditor = io.getAuditor();//订单审核人
		this.completeTime = io.getCompleteTime()!=null?DateUtil.formatDatetime(io.getCompleteTime().getTime()):"";//订单完成时间
		this.updateTime = DateUtil.formatDatetime(io.getUpdateTime().getTime());//订单修改时间
		this.orderDate = DateUtil.formatDate(io.getOrderDate().getTime());//订单日期
		this.assetPoolName = io.getAccount().getAssetPool().getName();
		if(io.getAccount().getProduct()!=null) {
			this.productName = io.getAccount().getProduct().getName();
		}
	}
	
	private String oid;
	private String orderCode;//订单号
	private String orderType;//交易类型
	private String orderCate;//订单类型
	private BigDecimal orderAmount;//订单金额	
	private BigDecimal orderVolume;//订单份额
	private String orderStatus;//订单状态
	private String entryStatus;//订单入账状态
	private String creater;//订单创建人
	private String createTime;//订单创建时间
	private String auditor;//订单审核人
	private String completeTime;//订单完成时间
	private String updateTime;//订单修改时间
	private String orderDate;//订单日期
	private String assetPoolName;//资产池名称
	private String productName;//产品名称

}
