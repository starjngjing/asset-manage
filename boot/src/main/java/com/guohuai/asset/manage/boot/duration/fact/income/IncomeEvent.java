package com.guohuai.asset.manage.boot.duration.fact.income;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.guohuai.asset.manage.boot.duration.assetPool.AssetPoolEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 收益分配事件表
 * @author wangyan
 *
 */
@Entity
@Table(name = "T_GAM_ASSETPOOL_INCOME_EVENT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeEvent implements Serializable {

	private static final long serialVersionUID = 4489598533252483250L;
	
	/**
	 * 状态 (待审核: CREATE;通过: PASS;驳回: FAIL;已删除: DELETE) 
	 */
	public static final String STATUS_Create = "CREATE";
	public static final String STATUS_Pass = "PASS";
	public static final String STATUS_Fail = "FAIL";
	public static final String STATUS_Delete = "DELETE";

	@Id
	private String oid;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assetPoolOid", referencedColumnName = "oid")
	private AssetPoolEntity assetPool;//关联资产池
	private Date baseDate; // 基准日
	private BigDecimal allocateIncome = new BigDecimal(0);//总分配收益
	private String creator;// 申请人
	private Timestamp createTime;  // 申请时间
	private String auditor;  // 审批人
	private Timestamp auditTime; // 审批时间
	private String status;// 状态 (待审核: CREATE;通过: PASS;驳回: FAIL;已删除: DELETE)

}
