package com.lind.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest(classes = { LettuceConnectionFactory.class })
public class RedisTemplateSerialization {

	public static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	RedisConnectionFactory factory;

	/**
	 * [ "com.lind.redis.RedisTemplateSerialization$User", { "id": 1, "name": "zzl" } ]
	 */
	@Test
	public void jsonObj() {

		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 日期序列化处理
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		om.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule())
				.registerModule(new ParameterNamesModule());

		// 存储java的类型，方便反序列化,没有这行，将存储为纯json字符串
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		extracted(template, "redis_test1");
		User user = (User) template.opsForValue().get("redis_test");
		log.info("user={}", user);
	}

	/**
	 * { "id": 1, "name": "zzl" }
	 */
	@Test
	public void jsonStr() throws IOException {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		// 序列化
		jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		extracted(template, "redis_test11");

		JsonParser jsonParser = mapper.getFactory()
				.createParser(mapper.writeValueAsBytes(template.opsForValue().get("redis_tesqt")));
		User jsonNode = jsonParser.readValueAs(User.class);
		log.info("user={}", jsonNode);
	}

	/**
	 * \xAC\xED\x00\x05sr\x00\x13com.lind.redis.User\xBD\xD6\x00)\x1E\x0F
	 * \x95\x02\x00\x02L\x00\x02idt\x00\x13Ljava/lang/Integer;L\x00\x04namet\x00\x12Ljava/lang/String;xpsr\x00\x11java.lang.Integer\x12\xE2\xA0\xA4\xF7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xAC\x95\x1D\x0B\x94\xE0\x8B\x02\x00\x00xp\x00\x00\x00\x01t\x00\x03zzl
	 */
	@Test
	public void hex() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		// 二进制序列化，实体类需要实现Serializable接口
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		// 序列化
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jdkSerializationRedisSerializer);
		template.setHashValueSerializer(jdkSerializationRedisSerializer);
		template.afterPropertiesSet();

		extracted(template, "redis_test111");
		User user = (User) template.opsForValue().get("redis_test111");
	}

	private void extracted(RedisTemplate<String, Object> template, String redis_tesqt) {
		User user = new User();
		user.setId(1);
		user.setName("zzl");
		user.setCreateTime(LocalDateTime.now());
		template.opsForValue().set(redis_tesqt, user);

	}

}
