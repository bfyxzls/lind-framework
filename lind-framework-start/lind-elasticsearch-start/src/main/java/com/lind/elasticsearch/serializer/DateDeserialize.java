package com.lind.elasticsearch.serializer;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

public class DateDeserialize extends JsonDeserializer {

	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

		String str = p.getText().trim();

		DateTime parse = DateUtil.parse(str);

		return new Date(parse.getTime());
	}

}
