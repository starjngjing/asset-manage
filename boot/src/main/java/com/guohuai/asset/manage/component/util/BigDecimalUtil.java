package com.guohuai.asset.manage.component.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static final BigDecimal init0 = BigDecimal.ZERO;
	public static final BigDecimal Num1 = new BigDecimal(1);
	public static final BigDecimal Num100 = new BigDecimal(100);
	public static final BigDecimal Num10000 = new BigDecimal(10000);
	
	public static BigDecimal formatForMul100(BigDecimal data) {
		return data.multiply(Num100).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal formatForDivide100(BigDecimal data) {
		return data.divide(Num100).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal formatForMul10000(BigDecimal data) {
		return data.multiply(Num10000).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal formatForDivide10000(BigDecimal data) {
		return data.divide(Num10000).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
}
