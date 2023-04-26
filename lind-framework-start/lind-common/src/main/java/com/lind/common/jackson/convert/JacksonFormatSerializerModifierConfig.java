package com.lind.common.jackson.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * MappingJackson2HttpMessageConverter注册器.
 * 自己默认如果有实现WebMvcConfigurationSupport,在方法configureMessageConverters里需要添加Set<MappingJackson2HttpMessageConverter>集合到converts.add里
 */
public class JacksonFormatSerializerModifierConfig extends AbstractJacksonSerializerModifier {

	@Autowired
	JacksonDateSerializerModifier jacksonDateSerializerModifier;

	@Autowired
	JacksonNullValueSerializerModifier jacksonNullValueSerializerModifier;

	@Bean
	@DependsOn("springContextUtils")
	public MappingJackson2HttpMessageConverter jacksonDateSerializerModifierConvert() {
		return super.callSelfSerializerModifier(jacksonDateSerializerModifier);
	}

	@Bean
	@DependsOn("springContextUtils")
	public MappingJackson2HttpMessageConverter jacksonNullValueSerializerModifierConvert() {
		return super.callSelfSerializerModifier(jacksonNullValueSerializerModifier);
	}

}
