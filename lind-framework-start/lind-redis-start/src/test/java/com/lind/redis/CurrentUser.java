package com.lind.redis;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author lind
 * @date 2022/7/13 11:21
 * @since 1.0.0
 */
public class CurrentUser implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.empty();
	}

}
