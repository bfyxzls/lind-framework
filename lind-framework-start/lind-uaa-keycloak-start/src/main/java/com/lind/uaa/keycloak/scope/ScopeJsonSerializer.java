package com.lind.uaa.keycloak.scope;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lind.uaa.keycloak.config.SecurityUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 自定义jackson序列化. 在应用的实体上使用注解 @JsonSerialize(using = ScopeJsonSerializer.class)
 *
 * @param <T>
 */
@Slf4j
public class ScopeJsonSerializer<T> extends JsonSerializer<T> {

	@SneakyThrows
	@Override
	public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		// 引用类型
		jsonGenerator.writeStartObject();
		for (Field field : t.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(ScopeSet.class)) {
				String value = field.getAnnotation(ScopeSet.class).value();
				if (Arrays.asList(SecurityUser.getScope()).contains(value)) {
					jsonGenerator.writeObjectField(field.getName(), field.get(t));
				}
			}
			else {
				jsonGenerator.writeObjectField(field.getName(), field.get(t));
			}
		}
		jsonGenerator.writeEndObject();
	}

}
