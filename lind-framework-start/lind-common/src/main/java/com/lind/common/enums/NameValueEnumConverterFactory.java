package com.lind.common.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * 枚举类型转换工厂.
 */
@Component
public class NameValueEnumConverterFactory implements ConverterFactory<String, NameValueEnum> {

	@Override
	public <T extends NameValueEnum> Converter<String, T> getConverter(Class<T> targetType) {
		return source -> {
			T ret = null;
			for (T t : targetType.getEnumConstants()) {
				if (t.getValue().equals(Integer.parseInt(source))) {
					ret = t;
				}
			}
			return ret;
		};
	}

}
