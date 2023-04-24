package com.lind.redis;

import com.lind.redis.lock.template.UserIdAuditorAware;

import java.util.Optional;

/**
 * @author lind
 * @date 2022/7/13 11:21
 * @since 1.0.0
 */
public class CurrentUser implements UserIdAuditorAware {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.empty();
	}

}
