package com.lind.common.thread.lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lind
 * @date 2023/6/21 13:11
 * @since 1.0.0
 */
public class CountDownLatchKc {

	/**
	 * CountDownLatch是Java中的一个同步工具类，它可以让一个或多个线程等待其他线程完成操作后再执行。CountDownLatch的两种使用场景如下：
	 *
	 * 场景1：让多个线程等待：模拟并发，让并发线程一起执行。为了模拟高并发，让一组线程在指定时刻执行抢购，这些线程在准备就绪后，进行等待
	 * (CountDownLatch.await
	 * ())，直到秒杀时刻的到来，然后一拥而上。在这个场景中，CountDownLatch充当的是一个“发令枪”的角色；就像田径赛跑时，运动员会在起跑线做准备动作，等到发令枪一声响，运动员就会奋力奔跑。
	 *
	 * 场景2：让单个线程等待：多个线程
	 * (任务)完成后，进行汇总合并。在实际开发场景中，很多情况下需要我们初始化一系列的前置操作，在这些准备条件都完成之前，是不能进行下一步工作的。我们可以让应用程序的主线程在其他线程都准备完毕之后再继续执行。
	 */
	AtomicInteger flag = new AtomicInteger();

	// 场景2
	@Test
	public void waitSubSystem() throws InterruptedException {
		Integer numThreads = 4;// 4个子任务需要处理完，程序才能向下执行.
		CountDownLatch startupLatch = new CountDownLatch(numThreads);
		// ...
		for (int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(() -> {
				// 并发操作代码
				flag.incrementAndGet();
				System.out.println(String.format("线程%s执行", Thread.currentThread().getId()));
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				startupLatch.countDown();// 自己的线程执行完成后，需要countDown()
			});
			thread.start();
		}
		// ...
		startupLatch.await();// 在需要等待的线程上执行这个await()，可以加超时时间
		assert flag.intValue() == 4;
		System.out.println("主程序开始执行");
	}

	// 场景1
	@Test
	public void scene1() throws InterruptedException {
		CountDownLatch startupLatch = new CountDownLatch(1);
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(() -> {
				try {
					startupLatch.await();// 等待这个信号
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println("子任务开始执行:" + Thread.currentThread().getId());
			});
			thread.start();
		}
		System.out.println("数据准备需要5秒");
		Thread.sleep(5000);
		startupLatch.countDown();// 发出信号
		System.out.println("子线程开始并发");
	}

}
