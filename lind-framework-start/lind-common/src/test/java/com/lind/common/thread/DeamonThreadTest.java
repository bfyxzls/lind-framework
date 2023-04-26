package com.lind.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/11/17 17:21
 * @since 1.0.0
 */
public class DeamonThreadTest {

	static Logger logger = LoggerFactory.getLogger(DeamonThreadTest.class);

	public static void main(String... args) throws InterruptedException {
		Thread scheduleThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.MILLISECONDS.sleep(10);
						logger.info("logger scheduleThread...");
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		scheduleThread.setDaemon(true);
		scheduleThread.setName("scheduleThread...");
		scheduleThread.start();// 主线程没有结束，它就不会结果.
		TimeUnit.MILLISECONDS.sleep(1000);
		logger.info("logger main stop...");
	}

}
