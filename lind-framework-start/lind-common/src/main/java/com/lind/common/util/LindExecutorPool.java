package com.lind.common.util;

import com.github.phantomthief.pool.Pool;
import com.github.phantomthief.pool.impl.ConcurrencyAwarePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * 线程池封装.
 */
@Slf4j
@Component
@ConditionalOnMissingBean(LindExecutorPool.class)
public class LindExecutorPool {

	/**
	 * 阻塞队列长度.
	 */
	private int queueSize = 10;

	/**
	 * 核心线程数.
	 */
	private int coreSize = 10;

	/**
	 * 最大线程数.
	 */
	private int maxSize = 20;

	/**
	 * 线程池中的消息有序性实现. https://github.com/PhantomThief/simple-pool
	 * https://zhuanlan.zhihu.com/p/138055401
	 * @return
	 */
	public Pool<Object> simplePool() {
		Pool<Object> pool = ConcurrencyAwarePool.builder().simpleThresholdStrategy(1, 0.7).build(String::new);
		return pool;
	}

	/**
	 * 默认线程池任务执行.
	 */
	public void defaultExecutor(Runnable runnable) {
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
		NameTreadFactory threadFactory = new NameTreadFactory();
		MyIgnorePolicy myIgnorePolicy = new MyIgnorePolicy();
		ExecutorService executor = new ThreadPoolExecutor(coreSize, maxSize, 1, MINUTES, queue, threadFactory,
				myIgnorePolicy);
		executor.execute(runnable);
	}

	/**
	 * 拒绝策略. 大于核心线核数放入阻塞队列，队列满了，建立新的线程，达到最大线程数时，走拒绝策略.
	 */
	static class MyIgnorePolicy implements RejectedExecutionHandler {

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			doLog(r, e);
		}

		private void doLog(Runnable r, ThreadPoolExecutor e) {
			// 可做日志记录等
			log.error(r.toString() + " rejected");
		}

	}

	/**
	 * 线程池. 阻塞队列满了，从这里取新的线程.
	 */
	static class NameTreadFactory implements ThreadFactory {

		private final AtomicInteger atomicInteger = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "my-thread-" + atomicInteger.getAndIncrement());
			log.debug(t.getName() + " has been created");
			return t;
		}

	}

}
