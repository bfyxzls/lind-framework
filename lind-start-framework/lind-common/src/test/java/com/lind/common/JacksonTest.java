package com.lind.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.lind.common.jackson.JsonSerialization;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class JacksonTest extends AbstractTest {

	private static final TypeReference<Map<String, String>> MAP_TYPE_REPRESENTATION = new TypeReference<Map<String, String>>() {
	};

	@SneakyThrows
	@Test
	public void kcSerializer() {
		Map<String, String> map = new HashMap<>();
		map.put("name", "zzl");
		map.put("address", "beijing");
		String result = JsonSerialization.writeValueAsString(map);
		log.info("result={}", result);
		Map<String, String> mapResult = JsonSerialization.readValue(result, MAP_TYPE_REPRESENTATION);
		log.info("mapResult={}", mapResult);
	}

	/**
	 * 字符串序列化
	 */
	@SneakyThrows
	@Test
	public void stringJackson() {
		DefaultResourceUser user = fromJson("jack.json", DefaultResourceUser.class);
		log.info("user:{}", user.getUsername());
		for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
			log.info("auth:{}", grantedAuthority.getAuthority());
		}

	}

	/**
	 * 具体类型序列化
	 */
	@SneakyThrows
	@Test
	public void objectJackson() {
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		User user = new User();
		user.setUsername("lind");
		user.setEmail("zzl@sina.com");
		String msg = om.writeValueAsString(user);
		log.info(msg);
		/**
		 * ["com.lind.common.JacksonTest$User",{"username":"lind","email":"zzl@sina.com","authorities":null}]
		 */
	}

	/**
	 * 序列化为具体类型，`无法反序列化接口`，即你用什么类型序列化的，就用什么类型反序列化
	 */
	@SneakyThrows
	@Test
	public void objectJacksonRead() {
		User user = fromJsonType("redis.json", User.class);
		log.info("user:{}", user.getUsername());
	}

	@JsonDeserialize(using = DefaultResourceUserSerializer.class)
	public interface DefaultResourceUser {

		String getUsername();

		String getEmail();

		Collection<? extends GrantedAuthority> getAuthorities();

	}

	public interface GrantedAuthority extends Serializable {

		String getAuthority();

	}

	@Data
	public static class User {

		private String username;

		private String email;

		private Collection<? extends GrantedAuthority> authorities;

	}

	public static class SimpleGrantedAuthority implements GrantedAuthority {

		private final String role;

		public SimpleGrantedAuthority(String role) {
			Assert.hasText(role, "A granted authority textual representation is required");
			this.role = role;
		}

		@Override
		public String getAuthority() {
			return role;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj instanceof SimpleGrantedAuthority) {
				return role.equals(((SimpleGrantedAuthority) obj).role);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return this.role.hashCode();
		}

		@Override
		public String toString() {
			return this.role;
		}

	}

	public static class DefaultResourceUserSerializer extends JsonDeserializer<DefaultResourceUser> {

		@Override
		public DefaultResourceUser deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			DefaultResourceUser userAccountAuthentication = new DefaultResourceUser() {
				@Override
				public String getUsername() {
					return node.get("username").asText();
				}

				@Override
				public String getEmail() {
					return node.get("email").asText();
				}

				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					List<GrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
					Iterator<JsonNode> elements = node.get("authorities").elements();
					while (elements.hasNext()) {
						JsonNode next = elements.next();
						JsonNode authority = next.get("authority");
						simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
					}
					return simpleGrantedAuthorities;
				}
			};
			return userAccountAuthentication;
		}

	}

}