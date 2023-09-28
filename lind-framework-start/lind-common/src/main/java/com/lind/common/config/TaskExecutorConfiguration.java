package com.lind.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池封装，通过注入的Executor对象和使用@Async注解的方法，都会享用这个功能.
 *
 * @author lengleng
 * @date 2022/5/20
 */
@Configuration
public class TaskExecutorConfiguration implements AsyncConfigurer {

	/**
	 * 获取当前机器的核数, 不一定准确 请根据实际场景 CPU密集,核心线程序数等于CPU，IO密集，核心线程数等于2*CPU
	 */
	public static final int cpuNum = Runtime.getRuntime().availableProcessors();

	@Value("${thread.pool.corePoolSize:}")
	private Optional<Integer> corePoolSize;

	@Value("${thread.pool.maxPoolSize:}")
	private Optional<Integer> maxPoolSize;

	@Value("${thread.pool.queueCapacity:}")
	private Optional<Integer> queueCapacity;

	@Value("${thread.pool.awaitTerminationSeconds:}")
	private Optional<Integer> awaitTerminationSeconds;

	@Override
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		// 核心线程大小 默认区 CPU 数量
		taskExecutor.setCorePoolSize(corePoolSize.orElse(cpuNum));
		// 最大线程大小 默认区 CPU * 2 数量
		taskExecutor.setMaxPoolSize(maxPoolSize.orElse(cpuNum * 2));
		// 队列最大容量
		taskExecutor.setQueueCapacity(queueCapacity.orElse(500));
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds.orElse(60));
		taskExecutor.setThreadNamePrefix("Lind-Thread-");
		taskExecutor.initialize();
		return taskExecutor;
	}

}
