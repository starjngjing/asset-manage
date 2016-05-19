package com.guohuai.asset.manage.boot.product;

import java.util.HashMap;
import java.util.Map;

public class ProductEnum {

	public static Map<String,String> enums = new HashMap<String,String>();
	static{
		enums.put("YES", "有");
		enums.put("NO", "无");
		enums.put("R1", "R1(谨慎型)");
		enums.put("R2", "R2(稳健型)");
		enums.put("R3", "R3(平衡型)");
		enums.put("R4", "R4(进取型)");
		enums.put("R5", "R5(激进型)");
		enums.put("CNY", "人民币");
	    enums.put("USD", "美元");
	    enums.put("EUR", "欧元");
	    enums.put("JPY", "日元");
	    enums.put("GBP", "英镑");
	    enums.put("HKD", "港币");
	    enums.put("SGD", "新元");
	    enums.put("AUD", "澳元");
	    enums.put("DAY", "每日");
	    enums.put("WEEK", "每周");
	    enums.put("MONTH", "每月");
	    enums.put("YEAR", "每年");
	    enums.put("NATRUE", "自然日");
	    enums.put("自然日", "交易日");
		
	}
}
