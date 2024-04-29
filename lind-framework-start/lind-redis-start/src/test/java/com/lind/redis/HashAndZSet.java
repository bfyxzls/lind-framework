package com.lind.redis;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;

/**
 * @author lind
 * @date 2024/4/28 9:42
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class })
public class HashAndZSet {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void hashsetExpire1() {
		redisTemplate.expire("h_set", Duration.ofMinutes(1));// 无效，无有效期，-1永久有效
		redisTemplate.opsForHash().put("h_set", "a", "1");
	}

	@Test
	public void hashsetExpire2() throws InterruptedException {
		redisTemplate.opsForHash().put("h_set2", "a", "1");
		redisTemplate.expire("h_set2", Duration.ofMinutes(1));// 有效，有效期1分
	}

	@Test
	public void hashsetExpire3() throws InterruptedException {
		redisTemplate.opsForHash().put("h_set3", "a", "1");
		redisTemplate.expire("h_set3", Duration.ofMinutes(1));// 有效，有效期1分
		redisTemplate.opsForHash().put("h_set3", "b", "2");
		Thread.sleep(30000L);
		redisTemplate.expire("h_set3", Duration.ofMinutes(1));// 多次设置时，以最后一次为准，这时有效期重设为1分
		redisTemplate.opsForHash().put("h_set3", "c", "3");
	}

	@Test
	public void hashsetExpire4() throws InterruptedException {
		redisTemplate.opsForHash().put("h_set4", "a", "1");
		redisTemplate.opsForHash().put("h_set4", "b", "2");
		redisTemplate.opsForValue().set("h_set4:b", "2");
		redisTemplate.expire("h_set4:b", Duration.ofSeconds(10));// 多次设置时，以最后一次为准，这时有效期重设为1分
		Thread.sleep(1000 * 60);

	}

	@Test
	public void zSetExpire1() {
		redisTemplate.expire("z_set", Duration.ofMinutes(1));// 无效，无有效期，-1永久有效
		redisTemplate.opsForZSet().add("z_set", "a", 1);
	}

	@Test
	public void zSetExpire2() throws InterruptedException {
		redisTemplate.opsForZSet().add("z_set2", "a", 1);
		redisTemplate.expire("z_set2", Duration.ofMinutes(1));
		Thread.sleep(1000 * 10);
		redisTemplate.opsForZSet().add("z_set2", "b", 2);
		redisTemplate.expire("z_set2", Duration.ofMinutes(1)); // 多次设置时，以最后一次为准，这时有效期重设为1分

	}

	@Test
	public void delZetByRange() {
		// ZREMRANGEBYSCORE key -inf <min_score> //删除分数小于min_score的元素
		log.info("z_set len:{}", redisTemplate.opsForZSet().zCard("z_set"));
		log.info("z_set2 len:{}", redisTemplate.opsForZSet().zCard("z_set2"));
		log.info("request_bucket2 len:{}", redisTemplate.opsForZSet().zCard("request_bucket2"));

		// 获取当前时间戳
		Instant currentInstant = Instant.now();
		long currentTimeMillis = currentInstant.toEpochMilli();

		// 计算1天前的时间戳
		Instant oneDayAgoInstant = currentInstant.minusSeconds(86400);
		long oneDayAgoTimeMillis = oneDayAgoInstant.toEpochMilli();

		redisTemplate.opsForZSet().removeRangeByScore("request_bucket2", 0, oneDayAgoTimeMillis);
		log.info("request_bucket2 len:{}", redisTemplate.opsForZSet().zCard("request_bucket2"));

	}

}
