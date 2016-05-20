package com.guohuai.asset.manage.boot.cashtool.log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guohuai.asset.manage.boot.cashtool.CashTool;
import com.guohuai.asset.manage.boot.cashtool.CashToolDao;
import com.guohuai.asset.manage.boot.enums.TargetEventType;
import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.util.DateUtil;

@Service
@Transactional
public class CashToolLogService {

	@Autowired
	private CashToolLogDao cashToolLogDao;
	@Autowired
	private CashToolDao cashToolDao;


	/**
	 * 添加标的操作日志
	 * @Title: saveInvestmentLog 
	 * @author vania
	 * @version 1.0
	 * @see: TODO
	 * @param targetOid
	 * @param operator
	 * @param eventType
	 * @return TargetLog    返回类型
	 */
	public CashToolLog saveCashToolLog(String targetOid, TargetEventType eventType, String operator) {
		if (StringUtils.isBlank(targetOid))
			throw AMPException.getException("投资标的id不能为空");
		CashTool cashTool = this.cashToolDao.findOne(targetOid);
		if (null == cashTool)
			throw AMPException.getException("找不到id为[" + targetOid + "]的投资标的");

		return this.saveCashToolLog(cashTool, eventType, operator);
	}
	
	/**
	 * 添加标的操作日志
	 * @Title: saveCashToolLog 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param cashTool
	 * @param eventType
	 * @param operator
	 * @return CashToolLog    返回类型
	 */
	public CashToolLog saveCashToolLog(CashTool cashTool, TargetEventType eventType, String operator) {
		if(null == eventType)
			throw AMPException.getException("操作类型不能为空!");
		CashToolLog entity = CashToolLog.builder().cashTool(cashTool).eventTime(DateUtil.getSqlCurrentDate())
				.eventType(eventType.name()) // 用name吧 直观一点
//				.eventType(eventType.ordinal() + "")
//				.eventType(eventType.getCode())
				.operator(operator)
				.build();
		return cashToolLogDao.save(entity);
	}
	
	
}
