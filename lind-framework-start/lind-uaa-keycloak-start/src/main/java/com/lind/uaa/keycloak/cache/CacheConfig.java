package com.lind.uaa.keycloak.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lind
 * @date 2023/3/10 14:25
 * @since 1.0.0
 */
@Configuration
public class CacheConfig {

	@Bean
	@ConditionalOnProperty(value = "keycloak.uaa.cache.redis.enabled", matchIfMissing = true)
	public CacheProvider redisCacheProvider(RedisTemplate<String, String> redisTemplate) {
		return new RedisCacheProvider(redisTemplate);
	}

}
