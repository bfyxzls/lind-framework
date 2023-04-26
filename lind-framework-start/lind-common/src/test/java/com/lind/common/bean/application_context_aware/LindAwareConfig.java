package com.lind.common.bean.application_context_aware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2022/9/9 9:43
 * @since 1.0.0
 */
@Configuration
public class LindAwareConfig {

	@Bean
	public LindAware testAware() {
		return new LindAware();
	}

}
