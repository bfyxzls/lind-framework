package com.lind.common.core.jackson.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期格式化. jackson实体字段修改器,只在第一次序列化实体字段时执行一次,以后不再执行.
 */
@Slf4j
public class JacksonDateSerializerModifier extends BeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		for (Object beanProperty : beanProperties) {
			BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
			Class<?> clazz = writer.getType().getRawClass();
			if (clazz.equals(Date.class)) {
				writer.assignSerializer(new DateSerializer());
			}
		}
		return beanProperties;
	}

	/**
	 * 格式化日期
	 */
	public class DateSerializer extends JsonSerializer<Object> {

		@Override
		public void serialize(Object date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			if (date == null) {
				jsonGenerator.writeNumber(StringUtils.EMPTY);
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
			format.setTimeZone(timeZone);
			String dateString = "\"" + format.format(date) + "\"";
			jsonGenerator.writeNumber(dateString);
		}

	}

}
