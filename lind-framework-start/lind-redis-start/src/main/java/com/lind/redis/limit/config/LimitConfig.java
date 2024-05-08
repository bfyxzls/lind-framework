package com.lind.redis.limit.config;

import com.lind.redis.limit.aop.LimitRaterInterceptor;
import com.lind.redis.limit.aop.RedisRaterLimiter;
import com.lind.redis.util.RedisRateLimiterPolice;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * EnableConfigurationProperties注册LimitProperties的bean 他里面的bean以来于lind.limit.enable的值.
 */
@EnableConfigurationProperties(LimitProperties.class)
@ConditionalOnProperty(value = "lind.rate-limit.enabled", matchIfMissing = true)
@Configuration
public class LimitConfig implements WebMvcConfigurer {

	/**
	 * ConditionalOnProperty条件满足时注册自己.
	 * @return
	 */
	@Bean
	public LimitRaterInterceptor limitRaterInterceptor() {
		return new LimitRaterInterceptor();
	}

	/**
	 * ConditionalOnProperty条件满足时注册自己.
	 * @return
	 */
	@Bean
	public RedisRaterLimiter redisRaterLimiter(StringRedisTemplate redisTemplate) {
		return new RedisRaterLimiter(redisTemplate);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(limitRaterInterceptor()).excludePathPatterns("/static/**").addPathPatterns("/**");
	}

	@Bean
	public RedisRateLimiterPolice redisRateLimiterPolice(RedisTemplate redisTemplate, RedissonClient redisson) {
		return new RedisRateLimiterPolice(redisTemplate, redisson);
	}

}
