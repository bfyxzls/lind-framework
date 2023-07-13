package com.lind.common.core.thread;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 共享变量的线程池.
 *
 * @author lind
 * @date 2023/1/29 15:19
 * @since 1.0.0
 */
public class ThreadPoolExecutorMdcWrapper extends ThreadPoolTaskExecutor {

	private static final long serialVersionUID = 3940722618853093830L;

	@Override
	public void execute(Runnable task) {
		super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
	}

}
