package com.lind.common.core.thread;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 在产生新线程时，将mdc信息复制到新线程里.
 *
 * @author lind
 * @date 2023/1/29 15:18
 * @since 1.0.0
 */
public class ThreadMdcUtil {

	public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
		return () -> {
			if (context == null) {
				MDC.clear();
			}
			else {
				MDC.setContextMap(context);
			}
			try {
				return callable.call();
			}
			finally {
				MDC.clear();
			}
		};
	}

	public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
		return () -> {
			if (context == null) {
				MDC.clear();
			}
			else {
				MDC.setContextMap(context);
			}
			try {
				runnable.run();
			}
			finally {
				MDC.clear();
			}
		};
	}

}
