package com.lind.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lind
 * @date 2023/7/19 14:06
 * @since 1.0.0
 */
public class RunnableTest {

	ExecutorService executorService;

	public static void main(String[] args) {
		RunnableTest runnableTest = new RunnableTest();
		runnableTest.executorService = Executors.newFixedThreadPool(2);
		runnableTest.executorService.submit(runnableTest.new job());
	}

	class job implements Runnable {

		@Override
		public void run() {
			System.out.println("hello world");
		}

	}

}
