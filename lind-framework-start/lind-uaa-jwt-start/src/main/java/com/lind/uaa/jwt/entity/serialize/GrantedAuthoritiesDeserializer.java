package com.lind.uaa.jwt.entity.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lind.uaa.jwt.entity.RoleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 对于标准的GrantedAuthority集合的解析，由于GrantedAuthority是个接口类型，所以需要自定义反序列化方式.
 */
public class GrantedAuthoritiesDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

	@Override
	public List<GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
		List<GrantedAuthority> roleGrantedAuthority = new ArrayList<>();

		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		Iterator<JsonNode> elements = node.elements();

		while (elements.hasNext()) {
			JsonNode next = elements.next();
			String id = next.get("id").asText();
			String name = next.get("name").asText();
			roleGrantedAuthority.add(new RoleGrantedAuthority(name, id));
		}
		return roleGrantedAuthority;
	}

}
