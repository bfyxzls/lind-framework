package com.lind.uaa.jwt.entity.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lind.uaa.jwt.entity.ResourceRole;
import com.lind.uaa.jwt.entity.ResourceUser;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

/**
 * 资源对象序列化时，不对密码序列化.
 */
public class ResourceUserSerializer extends JsonSerializer<ResourceUser> {

	@Override
	public void serialize(ResourceUser resourceUser, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (resourceUser != null) {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("id", resourceUser.getId());
			jsonGenerator.writeStringField("username", resourceUser.getUsername());
			jsonGenerator.writeStringField("email", resourceUser.getEmail());

			// 重写getAuthorities，主要在AccessDecisionManager.decide中使用.
			if (!CollectionUtils.isEmpty(resourceUser.getResourceRoles())) {
				jsonGenerator.writeArrayFieldStart("authorities");
				for (ResourceRole role : resourceUser.getResourceRoles()) {
					jsonGenerator.writeStartObject();
					jsonGenerator.writeStringField("id", role.getId());
					jsonGenerator.writeStringField("name", role.getName());
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndArray();
			}
			jsonGenerator.writeEndObject();
		}
	}

}
