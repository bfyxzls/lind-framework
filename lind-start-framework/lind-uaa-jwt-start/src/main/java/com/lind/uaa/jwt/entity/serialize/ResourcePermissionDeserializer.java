package com.lind.uaa.jwt.entity.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lind.uaa.jwt.entity.ResourcePermission;

import java.io.IOException;
import java.util.List;

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
			public String getId() {
				return node.get("id").asText();
			}

			@Override
			public String getTitle() {
				return node.get("title").asText();
			}

			@Override
			public String getUrl() {
				return node.get("url").asText();
			}

			@Override
			public String getParentId() {
				return node.get("parentId").asText();
			}

			@Override
			public Integer getType() {
				return node.get("type").asInt();
			}

			@Override
			public String getPermissions() {
				return node.get("permissions").asText();
			}

			@Override
			public List<? extends ResourcePermission> getSons() {
				return null;
			}

			@Override
			public void setSons(List<? extends ResourcePermission> resourcePermissions) {

			}
		};

		return defaultSourcePermission;
	}

}
