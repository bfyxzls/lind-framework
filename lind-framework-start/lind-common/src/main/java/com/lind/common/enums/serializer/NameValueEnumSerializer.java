package com.lind.common.enums.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lind.common.enums.NameValueEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 自定义序列化器,对前端输出的JSON只包含名称即可，默认是一个k/v对象.
 */
public class NameValueEnumSerializer extends JsonSerializer<NameValueEnum> {

	@Override
	public void serialize(NameValueEnum value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		String output = StringUtils.EMPTY;
		if (value != null) {
			output = value.getName();
		}
		jsonGenerator.writeString(output);
	}

}
