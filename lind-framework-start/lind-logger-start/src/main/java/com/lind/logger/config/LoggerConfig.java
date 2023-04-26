package com.lind.logger.config;

import com.lind.logger.aspect.LogRecordAspect;
import com.lind.logger.service.CurrentIpAware;
import com.lind.logger.service.CurrentUserAware;
import com.lind.logger.service.LoggerService;
import com.lind.logger.service.impl.DefaultCurrentIpAware;
import com.lind.logger.service.impl.DefaultCurrentUserAware;
import com.lind.logger.service.impl.DefaultLoggerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

	@Bean
	public LogRecordAspect logRecordAspect() {
		return new LogRecordAspect();
	}

	@Bean
	@ConditionalOnMissingBean(LoggerService.class)
	public LoggerService defaultLoggerService() {
		return new DefaultLoggerService();
	}

	@Bean
	@ConditionalOnMissingBean(CurrentUserAware.class)
	public CurrentUserAware defaultCurrentUserAware() {
		return new DefaultCurrentUserAware();
	}

	@Bean
	@ConditionalOnMissingBean(CurrentIpAware.class)
	public CurrentIpAware defaultCurrentIpAware() {
		return new DefaultCurrentIpAware();
	}

}
