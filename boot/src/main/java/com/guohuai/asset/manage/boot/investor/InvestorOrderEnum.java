package com.guohuai.asset.manage.boot.investor;

import java.util.HashMap;
import java.util.Map;

public class InvestorOrderEnum {

	public static Map<String,String> enums = new HashMap<String,String>();
	static{
		/**
		 * 交易类型orderType：
		 */
		enums.put("INVEST", "申购");
		enums.put("REDEEM", "赎回");
	    enums.put("BUY_IN", "买入");
	    enums.put("PART_SELL_OUT", "部分卖出");
	    enums.put("FULL_SELL_OUT", "全部卖出");
	    
	    /**
		 * 订单类型orderCate：
		 */
	    enums.put("TRADE", "交易订单");
	    enums.put("STRIKE", "冲账订单");
	    
	    /**
		 * 订单状态orderStatus：
		 */
	    enums.put("SUBMIT", "未确认");
	    enums.put("CONFIRM", "确认");
	    enums.put("DISABLE", "失效");
	    enums.put("CALCING", "清算中");
	    
	    /**
		 * 订单入账状态entryStatus：
		 */
	    enums.put("YES", "已入账");
		enums.put("NO", "未入账");
		
		/**
		 * 订单来源orderStem：
		 */
	    enums.put("USER", "用户申请");
	    enums.put("PLATFORM", "平台补录");
		
	}
	
}
