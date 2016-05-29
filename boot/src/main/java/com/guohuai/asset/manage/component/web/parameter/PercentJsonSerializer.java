package com.guohuai.asset.manage.component.web.parameter;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 百分比序列化
 * <p>Title: JavaPercentJsonSerializer.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月29日 上午8:10:59
 */
public class PercentJsonSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
		if (null != arg0) {
			arg1.writeNumber(arg0.multiply(new BigDecimal(100)));
		}
	}

}
