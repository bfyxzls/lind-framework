package com.lind.fileupload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lind
 * @date 2024/5/31 16:48
 * @since 1.0.0
 */
@Configuration
public class AutoRegisterConfig {

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
