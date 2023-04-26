package com.lind.uaa.keycloak.permission.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lind.uaa.keycloak.permission.ResourcePermission;

import java.io.IOException;

/**
 * DefaultSourcePermission反序列化工具
 */
public class ResourcePermissionDeserializer extends JsonDeserializer<ResourcePermission> {

	@Override
	public ResourcePermission deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		ResourcePermission defaultSourcePermission = new ResourcePermission() {

			@Override
			public String getTitle() {
				return node.get("title").asText();
			}

			@Override
			public String getPath() {
				return node.get("path").asText();
			}

		};

		return defaultSourcePermission;
	}

}
