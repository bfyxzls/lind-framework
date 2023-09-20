package com.lind.logback.config;

import com.lind.logback.mdc.LogInterceptor;
import com.lind.logback.mdc.ThreadPoolExecutorMdcWrapper;
import com.lind.logback.trace.DefaultTraceGenerator;
import com.lind.logback.user.AnonymousCurrentUser;
import com.lind.logback.user.CurrentUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lind
 * @date 2023/1/28 11:14
 * @since 1.0.0
 */
@Configuration
public class LoggerConfig {

	// 最大可用的CPU核数
	public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

	@Bean
	@Primary
	public ThreadPoolTaskExecutor getExecutor() {
		ThreadPoolExecutorMdcWrapper executor = new ThreadPoolExecutorMdcWrapper();
		executor.setCorePoolSize(PROCESSORS * 2);
		executor.setMaxPoolSize(PROCESSORS * 4);
		executor.setQueueCapacity(50);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("Task-A");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();
		return executor;
	}

	@Bean
	@ConditionalOnMissingBean(CurrentUser.class)
	public CurrentUser currentUser() {
		return new AnonymousCurrentUser();
	}

	@Bean
	@ConditionalOnMissingBean(DefaultTraceGenerator.class)
	public DefaultTraceGenerator traceGenerator() {
		return new DefaultTraceGenerator();
	}

	@Bean
	public LogInterceptor logInterceptor(CurrentUser currentUser) {
		return new LogInterceptor(currentUser);
	}

}
