package com.lind.common.util;

/**
 * 操作重新处理.
 */
public class RetryUtils {

	static final int RETRY_COUNT = 5;

	/**
	 * 重试操作，一次比一次等待的时间长.
	 * @param runnable
	 */
	public static void run(Runnable runnable) {
		int retries = 0;
		while (retries < RETRY_COUNT) {
			try {
				runnable.run();
				break;
			}
			catch (Exception e) {
				retries++;
				try {
					Thread.sleep(1000 * retries);
				}
				catch (InterruptedException e1) {
				}
			}
		}
	}

}
