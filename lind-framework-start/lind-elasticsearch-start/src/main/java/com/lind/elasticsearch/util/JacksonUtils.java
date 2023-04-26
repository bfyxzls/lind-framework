package com.lind.elasticsearch.util;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class JacksonUtils {

	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {

		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
		// ObjectMapper.DefaultTyping
		// .NON_FINAL);

		// 常用配置
		OBJECT_MAPPER.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
		OBJECT_MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), false);

		// 允许非转义字符，
		OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
		// 允许换行符 \n
		OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);

		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// 是否允许数字以0开头，如果为true，那么数字0001将会转成0
		OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature(), false);

		// 大小写脱敏 默认为false 需要改为true
		OBJECT_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addSerializer(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

		javaTimeModule.addSerializer(Date.class,
				new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));

		javaTimeModule.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalDate.class,
				new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy" + "-MM-dd")));
		javaTimeModule.addDeserializer(LocalTime.class,
				new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm" + ":ss")));

		OBJECT_MAPPER.registerModule(javaTimeModule);

		SimpleModule simpleModule = new SimpleModule();
		// 时间序列化
		// 时间反序列化
		simpleModule.addDeserializer(Date.class, new MyDateDeserialize());

		OBJECT_MAPPER.registerModule(simpleModule);

		OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	public static class MyDateDeserialize extends JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

			String str = p.getText().trim();

			// 解析时间戳模式
			if (StringUtils.isNotBlank(str) && Pattern.matches("\\d{13}", str)) {
				return new Date(Long.parseLong(str));
			}

			try {
				return new Date(DateUtil.parse(str).getTime());
			}
			catch (Exception e) {
				try {
					return DateUtil.parse(str, "yyyy.MM");
				}
				catch (Exception exception) {
					return DateUtil.parse(str, "yyyy");
				}
			}

		}

	}

}
