package com.lind.uaa.keycloak.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lind
 * @date 2023/3/10 14:20
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class RedisCacheProvider implements CacheProvider {

	final RedisTemplate<String, String> redisTemplate;

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void add(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

}
