package com.guohuai.asset.manage.boot.product.reward;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 奖励收益设置实体
 * 
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_INCOME_REWARD")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductIncomeReward implements Serializable {

	private static final long serialVersionUID = 872101518936561256L;
	
	@Id
	private String oid;// 序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productOid", referencedColumnName = "oid")
	private Product product;//关联产品

	private Integer startDate = 0;//起始天数	
	private Integer endDate = 0;//截止天数
	private BigDecimal ratio = new BigDecimal(0);//奖励收益率

}
