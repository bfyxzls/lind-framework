package com.lind.elasticsearch.serializer;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

	private final String pattern;

	public DateSerializer(String pattern) {
		this.pattern = pattern;
	}

	public DateSerializer() {
		this.pattern = "yyyy-MM-dd HH:mm:ss";
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {

		String format = DateUtil.format(value, pattern);

		gen.writeString(format);
	}

}
