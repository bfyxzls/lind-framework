package com.lind.elasticsearch;

import com.lind.elasticsearch.audit.EsAuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 得到当前操作人信息.
 */
@Component
public class UserAuditorAware implements EsAuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("1");
	}

}
