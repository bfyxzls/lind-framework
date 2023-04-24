package com.lind.uaa.jwt.utils;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 权限获取工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {

	/**
	 * 获取用户ID
	 */
	public static Long getUserId() {
		return UserContextHolder.getUserId();
	}

	/**
	 * 获取用户名称
	 */
	public static String getUsername() {
		return UserContextHolder.getUserName();
	}

	/**
	 * 获取用户
	 * @return
	 */
	public static UserDetails getUser() {
		return UserContextHolder.getUser();
	}

}
