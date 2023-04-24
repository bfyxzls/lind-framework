package com.lind.common.thread;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedTest {

	// 考虑一般缓存行大小是64字节，一个 long 类型占8字节
	static long[][] arr;

	private static int count = 0;

	/**
	 * 线程安全的integer（CAS方式，由CPU控制)
	 */
	private static AtomicInteger atomicInteger = new AtomicInteger(0);

	/**
	 * 缓存行的测试，对连续内存的操作.
	 */
	@Test
	public void cacheLine() {
		arr = new long[1024 * 1024][];
		for (int i = 0; i < 1024 * 1024; i++) {
			arr[i] = new long[8];
			for (int j = 0; j < 8; j++) {
				arr[i][j] = 0L;
			}
		}
		long sum = 0L;
		long marked = System.currentTimeMillis();
		for (int i = 0; i < 1024 * 1024; i += 1) {
			for (int j = 0; j < 8; j++) {
				sum = arr[i][j];
			}
		}
		System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");

		marked = System.currentTimeMillis();
		for (int i = 0; i < 8; i += 1) {
			for (int j = 0; j < 1024 * 1024; j++) {
				sum = arr[j][i];
			}
		}
		System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");
	}

	@Test
	public void syncTest() throws InterruptedException {
		Runnable t1 = new MyThread();
		for (int i = 0; i < 5; i++) {
			new Thread(t1, "thread" + i).start();
		}
		Thread.sleep(1000);
		// 保证结果是5000000
		System.out.println("result: " + count);
	}

	@Test
	public void noSyncTest() throws InterruptedException {
		Runnable t1 = new NoSync();
		for (int i = 0; i < 5; i++) {
			new Thread(t1, "thread" + i).start();
		}
		Thread.sleep(1000);
		// 产生并发后，结果<5000000
		System.out.println("result: " + count);
		// atomicInteger结果为5000000
		System.out.println("atomicInteger result: " + atomicInteger.getAndIncrement());
	}

	/**
	 * 未加锁
	 */
	class NoSync implements Runnable {

		@SneakyThrows
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName());
			for (int i = 0; i < 1000000; i++) {
				count++;
				atomicInteger.getAndIncrement();
			}

		}

	}

	/**
	 * 加锁(LOCK方式）
	 */
	class MyThread implements Runnable {

		@Override
		public void run() {
			synchronized (this) {
				System.out.println(Thread.currentThread().getName());
				for (int i = 0; i < 1000000; i++)
					count++;
			}

		}

	}

}
