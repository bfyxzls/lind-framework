package com.lind.common.bean.applicationcontextaware_init_disposable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LindConfig {

	@Bean
	public LindFactory lindFactory() {
		return new LindFactory();
	}

}
