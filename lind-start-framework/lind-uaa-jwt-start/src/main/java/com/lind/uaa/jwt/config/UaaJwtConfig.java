package com.lind.uaa.jwt.config;

import com.lind.uaa.jwt.service.ResourcePermissionService;
import com.lind.uaa.jwt.service.impl.DefaultUserDetailsService;
import com.lind.uaa.jwt.service.impl.RedisResourcePermissionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UaaJwtConfig {

	/**
	 * 默认的UserDetailsService实现者,使用者没有实现就用这个.
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public UserDetailsService defaultUserDetailsService() {
		return new DefaultUserDetailsService();
	}

	/**
	 * 默认的ResourcePermissionService实现者,使用者没有实现就用这个，对于微服务体系，除了系统服务会实现自己的ResourcePermissionService，以些来通过数据库获取对应系统之外，其它服务，都使用默认的实现，从redis中获取权限信息.
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ResourcePermissionService.class)
	public RedisResourcePermissionService redisResourcePermissionService() {
		return new RedisResourcePermissionService();
	}

}
