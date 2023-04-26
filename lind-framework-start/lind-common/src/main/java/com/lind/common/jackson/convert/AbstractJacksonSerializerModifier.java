package com.lind.common.jackson.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Set;

/**
 * jackson序列化转换器基类,子类以SerializerModifier结尾.
 */
@Slf4j
public abstract class AbstractJacksonSerializerModifier {

	@Autowired
	private Set<BeanSerializerModifier> beanSerializerModifiers;

	/**
	 * 将自己的转换器添加到转换器列表里.
	 * @param beanSerializerModifier
	 * @return
	 */
	protected MappingJackson2HttpMessageConverter callSelfSerializerModifier(
			BeanSerializerModifier beanSerializerModifier) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = converter.getObjectMapper();
		mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(beanSerializerModifier));

		// 修改器的累加,否则会进行对之前修改器的复盖
		beanSerializerModifiers.forEach((o) -> {
			mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(o));
			log.info("list o:{}", o);
		});
		return converter;
	}

}
