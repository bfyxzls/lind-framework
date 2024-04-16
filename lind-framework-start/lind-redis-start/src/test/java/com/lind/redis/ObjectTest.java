package com.lind.redis;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@Slf4j
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class })
public class ObjectTest {

	@Autowired
	RedisTemplate redisTemplate;

	@Test
	public void write() {
		User user = new User();
		user.setId(1);
		user.setName("zzl");
		user.setCreateTime(LocalDateTime.now());
		redisTemplate.opsForValue().set("testObj2", user);
		redisTemplate.opsForValue().set("testObjString2", "hello");
	}

	@Test
	public void writeZSet() {
		redisTemplate.opsForZSet().add("zset_test1", "a", 1);
		redisTemplate.opsForZSet().add("zset_test2", "b", 2);
		redisTemplate.opsForZSet().add("zset_test3", "c", 3);
		redisTemplate.opsForZSet().rangeByScore("zset_test4", 1, 2).forEach(System.out::println);
	}

	@Test
	public void read() {
		System.out.println(redisTemplate.opsForValue().get("testObj"));
	}

}
