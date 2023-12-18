package com.lind.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lind
 * @date 2023/6/13 9:16
 * @since 1.0.0
 */
@Slf4j
public class InterruptTest {

	private ExecutorService executorService;

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(() -> {
			Thread c = Thread.currentThread();
			int i = 0, n = 0;
			while (!c.isInterrupted()) {
				i++;
				if (i % 100000000 == 0) {
					n++;
					System.out.println("第" + n + "次输出;");
				}
				if (i == 1000000000) {
					i = 1;
				}
			}
			if (c.isInterrupted()) {
				System.out.println("线程" + c.getName() + "检测到中断信号, 中断结束");
			}
		});

		t.start();

		Thread.sleep(1000);
		Thread mainThread = Thread.currentThread();
		System.out.println(mainThread + "请求将线程" + t.getName() + "中断");
		t.interrupt();
		System.out.println("中断请求已发出");
	}

	@Before
	public void setUp() {
		executorService = Executors.newFixedThreadPool(4);
	}

	@Test(timeout = 3000)
	public void testRepeat() {
		for (int i = 0; i < 4; i++) {
			executorService.submit(() -> {
				log.info("thread:{}", Thread.currentThread().getId());
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

}
