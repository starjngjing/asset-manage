package com.guohuai.asset.manage.component.config;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.guohuai.asset.manage.component.web.parameter.DateParam.DateArgumentResolver;
import com.guohuai.asset.manage.component.web.parameter.JavaDateJsonDeserializer;
import com.guohuai.asset.manage.component.web.parameter.JavaDateJsonSerializer;
import com.guohuai.asset.manage.component.web.parameter.SqlDateJsonDeserializer;
import com.guohuai.asset.manage.component.web.parameter.SqlDateJsonSerializer;
import com.guohuai.asset.manage.component.web.parameter.TimestampJsonDeserializer;
import com.guohuai.asset.manage.component.web.parameter.TimestampJsonSerializer;
import com.guohuai.asset.manage.component.web.parameter.TimestampParam.TimestampArgumentResolver;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;

@Configuration
public class RequestParamConfig {

	@Bean
	public WebMvcConfigurerAdapter configurerAdapter() {

		WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {

			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
				argumentResolvers.add(new SpecificationArgumentResolver());
				argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
				argumentResolvers.add(new DateArgumentResolver());
				argumentResolvers.add(new TimestampArgumentResolver());
			}

		};

		return adapter;

	}

	@Bean
	@Primary
	public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBeanConfig() {
		Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();

		Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<Class<?>, JsonDeserializer<?>>();
		deserializers.put(Timestamp.class, new TimestampJsonDeserializer());
		deserializers.put(java.sql.Date.class, new SqlDateJsonDeserializer());
		deserializers.put(java.util.Date.class, new JavaDateJsonDeserializer());
		bean.setDeserializersByType(deserializers);

		Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<Class<?>, JsonSerializer<?>>();
		serializers.put(Timestamp.class, new TimestampJsonSerializer());
		serializers.put(java.sql.Date.class, new SqlDateJsonSerializer());
		serializers.put(java.util.Date.class, new JavaDateJsonSerializer());
		bean.setSerializersByType(serializers);

		return bean;
	}

}
