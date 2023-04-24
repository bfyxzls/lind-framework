package com.lind.redis.lock.config;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import com.lind.redis.lock.aspect.RepeatSubmitAspect;
import com.lind.redis.lock.template.DefaultUserIdAuditorAwareImpl;
import com.lind.redis.lock.template.RedisLockTemplate;
import com.lind.redis.lock.template.RedisUserManualLockTemplate;
import com.lind.redis.lock.template.UserIdAuditorAware;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * redis lock bean register.
 * 同时注册RedisLockProperty这个bean，限制条件是lind.redis.lock.enable为true时注册它,如果不想使用它，在配置中enable为false即可.
 */
@EnableConfigurationProperties(RedisLockProperty.class)
@RequiredArgsConstructor
@ConditionalOnBean(LettuceRedisAutoConfigure.class) // 依赖注入
@ConditionalOnProperty(value = "lind.redis.lock.enabled", matchIfMissing = true)
public class RedisLockConfig {

	private final RedisLockProperty redisLockProperty;

	/**
	 * 没有其它LockRegistry类型的bean，就注册这个bean.
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(LockRegistry.class)
	public RedisLockRegistry redisLockRegistry(LettuceConnectionFactory redisConnectionFactory) {
		return new RedisLockRegistry(redisConnectionFactory, redisLockProperty.getRegistryKey());
	}

	@Bean
	@ConditionalOnBean(name = "redisTemplateString")
	public RepeatSubmitAspect repeatSubmitAspect(RedisTemplate redisTemplate, UserIdAuditorAware userIdAuditorAware) {
		return new RepeatSubmitAspect(redisTemplate, userIdAuditorAware);
	}

	@Bean
	@ConditionalOnBean(name = "redisTemplateString")
	public RedisLockTemplate redisLockTemplate(RedisLockRegistry redisLockRegistry,
			RedisLockProperty redisLockProperty) {
		return new RedisLockTemplate(redisLockRegistry, redisLockProperty);
	}

	@Bean
	@ConditionalOnBean(RedisLockTemplate.class)
	public RedisUserManualLockTemplate redisUserManualLockTemplate(RedisTemplate<String, String> redisTemplate,
			RedisLockProperty redisLockProperty, RedisLockTemplate redisLockTemplate) {
		return new RedisUserManualLockTemplate(redisTemplate, redisLockProperty, redisLockTemplate);
	}

	@Bean
	@ConditionalOnMissingBean(UserIdAuditorAware.class)
	public UserIdAuditorAware userIdAuditorAware() {
		return new DefaultUserIdAuditorAwareImpl();
	}

}
