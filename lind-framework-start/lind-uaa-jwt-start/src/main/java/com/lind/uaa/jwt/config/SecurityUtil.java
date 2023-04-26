package com.lind.uaa.jwt.config;

import com.lind.uaa.jwt.entity.ResourceUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 当前用户的上下文.
 */
@Component
@Slf4j
public class SecurityUtil {

	/**
	 * 获取当前登录用户
	 * @return
	 */
	@SneakyThrows
	public ResourceUser getCurrUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (ResourceUser) obj;
	}

}
