package com.guohuai.asset.manage.component.web.parameter;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 反序列化百分比
 * <p>Title: PercentJsonDeserializer.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月29日 上午8:15:41
 */
public class PercentJsonDeserializer extends JsonDeserializer<BigDecimal> {


	@Override
	public BigDecimal deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException {
		if (null == arg0.getText() || arg0.getText().toString().trim().equals("")) {
			return null;
		}
		System.out.println("反序列化百分比,原值:" + arg0.getText());
		BigDecimal b = new BigDecimal(arg0.getText());
		// BigDecimal b = arg0.getDecimalValue();
		return b.divide(new BigDecimal(100));

	}

}
