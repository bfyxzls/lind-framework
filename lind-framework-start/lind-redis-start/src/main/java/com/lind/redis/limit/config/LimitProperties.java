package com.lind.redis.limit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置属性,不会自动注册.
 */
@Data
@ConfigurationProperties(prefix = "lind.rate-limit")
public class LimitProperties {

	/**
	 * 是否开启全局限流.
	 */
	private Boolean enabled = false;

	/**
	 * 限制请求个数.
	 */
	private Integer limit = 100;

	/**
	 * 每单位时间内（毫秒）.
	 */
	private Integer timeout = 1000;

}
