package com.lind.mybatis;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 获取当前登陆的用户信息.
 */
@Component
public class UserAuditAware implements AuditorAware {

	@Override
	public Optional getCurrentAuditor() {
		return Optional.of("1");
	}

}
