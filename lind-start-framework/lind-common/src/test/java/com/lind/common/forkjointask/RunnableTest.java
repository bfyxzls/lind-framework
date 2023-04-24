package com.lind.common.forkjointask;

import com.lind.common.util.ListUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RunnableTest {

	static Lock lock = new ReentrantLock(); // 注意这个地方

	@SneakyThrows
	@Test
	public void test() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ExecutorService executor = Executors.newFixedThreadPool(30);
		Collection<DoUrl> bufferInserts = new ArrayList<>();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			list.add((String.valueOf(i)));
		}

		// 将大集合分成5份，每份20个数据，这样有5个线程就可以干完了，而咱们设置的线程数据是10，事实上有5个线程是空闲的
		// 线程初始化可以改成Executors.newFixedThreadPool(5)
		ListUtils.splitList(list, 10).forEach(o -> {
			bufferInserts.add(new DoUrl(o));
		});

		executor.invokeAll(bufferInserts);
		executor.shutdown();
		stopWatch.stop();
		System.out.println("stopwatch:" + stopWatch.getTotalTimeMillis());
	}

	/**
	 * 这块内容多线程执行有并发问题，需要考虑线程安全性问题. 加上锁了，但发挥不出并发的优势了，Callable失去了意义.
	 */
	class DoUrl implements Callable<Integer> {

		List<String> url;

		public DoUrl(List<String> url) {
			this.url = url;
		}

		@Override
		public Integer call() throws Exception {
			this.url.forEach(o -> {
				try {
					lock.lock();
					System.out.println("thread id:" + Thread.currentThread().getId() + " start");
					System.out.println("thread id:" + Thread.currentThread().getId() + ":" + o);
					System.out.println("thread id:" + Thread.currentThread().getId() + " end");
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally {
					lock.unlock();
				}
			});
			return 1;
		}

	}

}
