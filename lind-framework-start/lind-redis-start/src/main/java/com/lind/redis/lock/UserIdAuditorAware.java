package com.lind.redis.lock;

import java.util.Optional;

public interface UserIdAuditorAware {

	/**
	 * 获取当前登录的用户.
	 * @return
	 */
	Optional<String> getCurrentAuditor();

}
