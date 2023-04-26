package com.lind.common.enums.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lind.common.enums.NameValueEnum;

import java.io.IOException;

/**
 * 自定义反序列化器.
 */
public class NameValueEnumDeserializer extends JsonDeserializer<NameValueEnum> {

	@Override
	public NameValueEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		NameValueEnum nameValueEnum = new NameValueEnum() {
			@Override
			public String getName() {
				if (node.isValueNode()) {
					return node.get("name").asText();
				}
				else
					return node.asText();
			}

			@Override
			public Integer getValue() {
				if (node.isValueNode()) {
					return node.get("value").asInt();
				}
				else {
					return 1;
				}
			}
		};
		return nameValueEnum;
	}

}
