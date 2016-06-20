package com.guohuai.asset.manage.boot.duration.fact.income;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 * 收益分配表
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_ASSETPOOL_INCOME_ALLOCATE")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeAllocate implements Serializable {

	private static final long serialVersionUID = -9056237663427594519L;

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventOid", referencedColumnName = "oid")
	private IncomeEvent incomeEvent;//关联收益分配事件
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productOid", referencedColumnName = "oid")
	private Product product;//关联产品
	private Date baseDate; // 收益基准日
	private BigDecimal capital = new BigDecimal(0);//产品总规模
	private BigDecimal allocateIncome = new BigDecimal(0);//分配收益
	private BigDecimal rewardIncome = new BigDecimal(0);//奖励收益
	private BigDecimal ratio = new BigDecimal(0);//收益率

}
