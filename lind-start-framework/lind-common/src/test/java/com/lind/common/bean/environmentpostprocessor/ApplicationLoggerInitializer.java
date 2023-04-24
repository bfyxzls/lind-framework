package com.lind.common.bean.environmentpostprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 需要在META-INF/spring.factories里添加org.springframework.boot.env.EnvironmentPostProcessor
 *
 * @author lind
 * @date 2023/3/24 15:50
 * @since 1.0.0
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String appName = environment.getProperty("spring.application.name");
		String logBase = environment.getProperty("LOGGING_PATH", "logs");
		System.out.println("appName=" + appName);
		// spring boot admin 直接加载日志
		System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
