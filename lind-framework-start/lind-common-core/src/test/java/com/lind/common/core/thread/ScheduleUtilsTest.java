package com.lind.common.core.thread;

import org.junit.Test;

/**
 * @author lind
 * @date 2023/7/24 11:30
 * @since 1.0.0
 */
public class ScheduleUtilsTest {

	@Test
	public void runAfter5s() throws InterruptedException {
		System.out.println("start");
		ScheduleUtils.schedule("test", () -> {
			System.out.println("test");
		}, 5, java.util.concurrent.TimeUnit.SECONDS);
		Thread.sleep(10000);
	}

	@Test
	public void params() throws InterruptedException {
		ScheduleUtils.schedule("test", new MyTask("zzl"), 1, java.util.concurrent.TimeUnit.SECONDS);
		Thread.sleep(2000);
	}

	class MyTask implements Runnable {

		private String name;

		public MyTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println("name=" + name);
		}

	}

}
