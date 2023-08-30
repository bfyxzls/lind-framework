package com.lind.common.core.jackson.module;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 关于一个默认值的处理，比如null值，可以设置为0，空字符串等.
 */
public class DefaultValueModule extends SimpleModule {

	public DefaultValueModule() {
		super(PackageVersion.VERSION);
		this.setSerializerModifier(new JacksonNullValueSerializerModifier());
	}

	class JacksonNullValueSerializerModifier extends BeanSerializerModifier {

		@Override
		public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
				List<BeanPropertyWriter> beanProperties) {
			for (Object beanProperty : beanProperties) {
				BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
				if (isArrayType(writer)) {
					writer.assignNullSerializer(new NullArrayJsonSerializer());
				}
				else if (isNumberType(writer)) {
					writer.assignNullSerializer(new NullNumberJsonSerializer());
				}
				else if (isBooleanType(writer)) {
					writer.assignNullSerializer(new NullBooleanJsonSerializer());
				}
				else if (isStringType(writer)) {
					writer.assignNullSerializer(new NullStringJsonSerializer());
				}
				else if (isDateType(writer)) {
					writer.assignNullSerializer(new NullDateJsonSerializer());
				}
			}

			return beanProperties;
		}

		// 判断是否是数组
		private boolean isArrayType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
		}

		// 判断是否是字符串
		private boolean isStringType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
		}

		// 判断是否是number
		private boolean isNumberType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return Number.class.isAssignableFrom(clazz);
		}

		// 判断是否是boolean
		private boolean isBooleanType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz.equals(Boolean.class);
		}

		// 判断是否是date(util)
		private boolean isDateType(BeanPropertyWriter writer) {
			Class<?> clazz = writer.getType().getRawClass();
			return clazz.equals(Date.class);
		}

		// 处理返回数组类型的null值
		public class NullArrayJsonSerializer extends JsonSerializer<Object> {

			@Override
			public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
				if (value == null) {
					jgen.writeStartArray();
					jgen.writeEndArray();
				}
			}

		}

		// 处理返回字符串类型的null值
		public class NullStringJsonSerializer extends JsonSerializer<Object> {

			@Override
			public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
					throws IOException {
				jsonGenerator.writeString(StringUtils.EMPTY);
			}

		}

		// 处理返回date类型的null值
		public class NullDateJsonSerializer extends JsonSerializer<Object> {

			@Override
			public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
					throws IOException {
				jsonGenerator.writeString(StringUtils.EMPTY);
			}

		}

		// 处理返回数字类型的null值
		public class NullNumberJsonSerializer extends JsonSerializer<Object> {

			@Override
			public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
					throws IOException {
				jsonGenerator.writeNumber(0);
			}

		}

		// 处理返回布尔类型的null值
		public class NullBooleanJsonSerializer extends JsonSerializer<Object> {

			@Override
			public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
					throws IOException {
				jsonGenerator.writeBoolean(false);
			}

		}

	}

}
