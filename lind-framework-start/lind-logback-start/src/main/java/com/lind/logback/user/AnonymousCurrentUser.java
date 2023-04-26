package com.lind.logback.user;

/**
 * @author lind
 * @date 2023/1/28 11:09
 * @since 1.0.0
 */
public class AnonymousCurrentUser implements CurrentUser {

	@Override
	public String getUserName() {
		return "Anonymous";
	}

}
