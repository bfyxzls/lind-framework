package com.lind.common.thread.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 通过 Semaphore和CountDownLatch实现多个线程并发操作，并有一定的数量限制. 例如排除买票【车站只有10个窗口出票，有100个人在买票，就需要排队了】
 *
 * @author lind
 * @date 2023/6/21 16:21
 * @since 1.0.0
 */
@Slf4j
public class SemaphoreCountDownLatchTest {

	ExecutorService executorService = Executors.newCachedThreadPool();

	@Test
	public void buyTicket() throws InterruptedException {
		// 请求总数
		int clientTotal = 50;
		// 同时并发执行的线程数
		int threadTotal = 10;
		// 信号量，此处用于控制并发的线程数
		Semaphore semaphore = new Semaphore(threadTotal);
		CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
		for (int i = 0; i < clientTotal; i++) {
			int finalI = i;
			executorService.execute(() -> {
				// 执行此方法用于获取执行许可,当总计未释放的许可数不超过10时,允许通行，否则线程阻塞等待，直到获取到许可
				try {
					semaphore.acquire();
					log.info("{}开始买票，需要5秒处理", finalI);// 每并并行处理10个人，一共50个人，需要处理5次，每次5秒，需要25秒完成
					Thread.sleep(5000);
					// 释放许可
					semaphore.release();
					// 闭锁减一
					countDownLatch.countDown();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		countDownLatch.await();// 线程阻塞，等待这10个人完成购票后，再执行下面的代码
		executorService.shutdown();
		log.info("finished.");
	}

}
