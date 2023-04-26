package com.lind.uaa.keycloak.permission.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lind.uaa.keycloak.permission.ResourcePermission;

import java.io.IOException;

public class ResourcePermissionSerializer extends JsonSerializer<ResourcePermission> {

	@Override
	public void serialize(ResourcePermission resourcePermission, JsonGenerator jsonGenerator,
			SerializerProvider serializers) throws IOException {
		if (resourcePermission != null) {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("title", resourcePermission.getTitle());
			jsonGenerator.writeStringField("path", resourcePermission.getPath());
			jsonGenerator.writeEndObject();
		}
	}

}
