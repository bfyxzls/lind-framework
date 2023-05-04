package com.lind.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 线程上下文传递，hystrix的相关实现有兴趣可以看源码. 将主线程的对象通过NextHttpHeader并共享给feign生产的线程.
 */
@Slf4j
public class RequestContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		// 主程序中执行
		log.debug("RequestContextHystrixConcurrencyStrategy.getCopyOfContextMap thread:{}",
				Thread.currentThread().getId());
		return new ThreadLocalCallable<>(callable, NextHttpHeader.getCopyOfContextMap());
	}

	public static class ThreadLocalCallable<V> implements Callable<V> {

		private Callable<V> target;

		private Map<String, String> dic;

		public ThreadLocalCallable(Callable<V> target, Map<String, String> dic) {
			this.target = target;
			this.dic = dic;
		}

		@Override
		public V call() throws Exception {
			// 新线程中的执行
			log.debug("RequestContextHystrixConcurrencyStrategy.setContextMap thread:{}",
					Thread.currentThread().getId());
			if (this.dic != null) {
				NextHttpHeader.setContextMap(this.dic);
			}
			return target.call();
		}

	}

}
