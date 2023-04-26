package com.lind.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.function.Supplier;

/**
 * 代码运行时间记录.
 */
public class StopWatchUtils {

	static Logger logger = LoggerFactory.getLogger(StopWatchUtils.class);

	/**
	 * 记录运行时间.
	 * @param title
	 * @param runnable
	 */
	public static void runTimer(String title, Runnable runnable) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		runnable.run();
		stopWatch.stop();
		logger.debug("{} \t {}ms", title, stopWatch.getLastTaskTimeMillis());
	}

	/**
	 * 带有返回值的，记录运行时间
	 * @param title
	 * @param runnable
	 * @param <T>
	 * @return
	 */
	public static <T> T returnTimer(String title, Supplier<T> runnable) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		T result = runnable.get();
		stopWatch.stop();
		logger.info("{} \t {}ms", title, stopWatch.getLastTaskTimeMillis());
		return result;
	}

}
