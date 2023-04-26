package com.lind.logback.mdc;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.MDC;

import java.util.concurrent.Callable;

/**
 * 线程上下文传递，hystrix的相关实现有兴趣可以看源码， hystrix提供了这个口子可以处理线程间传值问题，这里不做过多赘述
 */
public class RequestContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

	@Override
	public <T> Callable<T> wrapCallable(final Callable<T> callable) {
		// 使用自定义的包装对象，将当前mdc复制到Hystrix新线程中
		return ThreadMdcUtil.wrap(callable, MDC.getCopyOfContextMap());
	}

}
