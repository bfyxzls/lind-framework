package com.lind.redis.lock.template;

import java.util.Optional;

/**
 * @author lind
 * @date 2022/10/24 11:36
 * @since 1.0.0
 */
public class DefaultUserIdAuditorAwareImpl implements UserIdAuditorAware {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.empty();
	}

}
