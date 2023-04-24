package com.lind.uaa.jwt.config;

public interface Constants {

	/**
	 * 在线用户
	 */
	String ONLINE_USER = "online-user:";

	/**
	 * 权限树
	 */
	String PERMISSION_ALL = "permission-all";

	/**
	 * 角色权限
	 */
	String ROLE_PERMISSION = "role-permission:";

	/**
	 * 登录失败次数限制
	 */
	String LOGIN_FAIL_LIMIT = "login-fail-limit:";

	/**
	 * 登录失败锁定时间限制
	 */
	String LOGIN_FAIL_LOCK_LIMIT = "login-fail-lock-limit:";

	/**
	 * 用户权限树
	 */
	String USER_PERMISSION = "user-permission:";

	/**
	 * 用户ID字段
	 */
	String DETAILS_USER_ID = "userId";

	/**
	 * 用户名字段
	 */
	String DETAILS_USERNAME = "username";

	/**
	 * 用户对象.
	 */
	String DETAILS_USER = "userDetail";

	/**
	 * 授权信息字段
	 */
	String AUTHORIZATION_HEADER = "authorization";

}
