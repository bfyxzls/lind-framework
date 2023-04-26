package com.lind.logback.user;

/**
 * @author lind
 * @date 2023/1/28 10:52
 * @since 1.0.0
 */
public interface CurrentUser {

	/**
	 * 获取当前登录的用户名.
	 * @return
	 */
	String getUserName();

}
