package com.lind.common.thread.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch共享锁,更多体现的组团的思想，同样是控制人数，但是需要够一窝.
 * 实现一个或者多个线程等待其它线程完成某个操作之后，再继续执行.
 *
 * @author lind
 * @date 2023/6/21 10:00
 * @since 1.0.0
 */
public class CountDownLatchTest {

	static class Worker implements Runnable {
		private final CountDownLatch latch;
		private final String name;

		public Worker(CountDownLatch latch, String name) {
			this.latch = latch;
			this.name = name;
		}

		@Override
		public void run() {
			try {
				// 模拟工作耗时
				Thread.sleep(2000);
				System.out.println("Worker " + name + " completed its task.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				// 完成任务后，调用countDown()方法减少计数器
				latch.countDown();
			}
		}
	}

	/**
	 * 在本示例中，我们创建了一个CountDownLatch对象，并指定计数器的初始值为3。然后，我们创建了3个工作线程，每个线程模拟一个任务并在完成后调用countDown()方法减少计数器。
	 * 主线程调用latch.await()方法，进入等待状态，直到所有工作线程完成任务并计数器归零后，主线程才会继续执行。
	 * 运行该程序，你会看到主线程在等待阶段输出了"Waiting for all workers to complete their tasks..."，然后等待3个工作线程完成任务后，
	 * 输出"All workers have completed their tasks. Proceeding..."。这说明主线程成功等待所有工作线程完成任务后再继续执行。
	 * 这个例子展示了如何使用CountDownLatch实现线程的等待和唤醒，让主线程在所有工作线程完成任务后再继续执行后续操作。
	 * @param args
	 * @throws InterruptedException
	 */
 		public static void main(String[] args) throws InterruptedException {
			int numWorkers = 3;
			CountDownLatch latch = new CountDownLatch(numWorkers);

			// 创建并启动多个工作线程
			for (int i = 1; i <= numWorkers; i++) {
				Thread workerThread = new Thread(new CountDownLatchTest.Worker(latch, "Worker " + i));
				workerThread.start();
			}

			System.out.println("Waiting for all workers to complete their tasks...");
			// 主线程等待所有工作线程完成任务
			latch.await(1, TimeUnit.SECONDS);//由于子线程1秒内没有完成，所以主程序就已经开始执行了，如果希望等待子线程执行完成，可以直接用latch.await()即可

			System.out.println("All workers have completed their tasks. Proceeding...");
			// 继续执行主线程的其他操作
		}


}
