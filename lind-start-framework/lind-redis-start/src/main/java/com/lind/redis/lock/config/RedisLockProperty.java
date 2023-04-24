package com.lind.redis.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 锁的配置属性,不自动注册，注册点到RedisLockConfig里,不要在spring.factories里注册了.
 */
@Data
@ConfigurationProperties(prefix = "lind.redis.lock")
public class RedisLockProperty {

	/**
	 * 是否开启redis分布锁.
	 */
	private Boolean enabled = true;

	/**
	 * 锁前缀.
	 */
	private String registryKey = "system-lock";

	/**
	 * 没有获到锁是否立即中断,true表示中断,false表示阻塞可重入锁.
	 */
	private Boolean interrupt = false;

	/**
	 * 手动锁键.
	 */
	private String manualLockKey = "user-lock";

}
