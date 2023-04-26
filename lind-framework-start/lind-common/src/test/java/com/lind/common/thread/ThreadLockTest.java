package com.lind.common.thread;

import cn.hutool.core.map.MapUtil;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadLockTest {

	private AtomicInteger isSnapshotFlushing = new AtomicInteger(0);

	private int notSnapshotFlushing = 0;

	/**
	 * 线程并发，读写锁.
	 */
	@SneakyThrows
	@Test
	public void threadLockPool() {
		int len = 5;

		List<RetryWriterThread> retryWriterThreads = new ArrayList<>();
		ExecutorService pool = Executors.newFixedThreadPool(5);

		for (int i = 0; i < len; i++) {
			retryWriterThreads.add(new RetryWriterThread(i, i));
		}
		pool.invokeAll(retryWriterThreads);
		pool.shutdown();
		System.out.println("end");
	}

	@Test
	public void map() {
		List<Map<String, Object>> maps = new ArrayList<>();
		maps.add(MapUtil.of("id", "1"));
		maps.add(MapUtil.of("id", "2"));
		maps.add(MapUtil.of("id", "3"));
		maps.add(MapUtil.of("id", "4"));
		maps.parallelStream().forEach(map -> {
			map.put("hello", "you are good");
			System.out.println(Thread.currentThread().getId());
		});
		System.out.println(maps);
	}

	/**
	 * 并发计算.
	 */
	@SneakyThrows
	@Test
	public void atomicInteger() {
		List<Callable<Integer>> retryWriterThreads = new ArrayList<>();
		ExecutorService pool = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 1500; i++) {
			retryWriterThreads.add(() -> {
				isSnapshotFlushing.getAndIncrement();
				return 0;
			});
		}

		for (int i = 0; i < 1500; i++) {
			retryWriterThreads.add(() -> {
				notSnapshotFlushing++;
				return 0;
			});
		}
		pool.invokeAll(retryWriterThreads);
		pool.shutdown();
		System.out.println("isSnapshotFlushing:" + isSnapshotFlushing.get());
		System.out.println("noSnapshotFlushing:" + notSnapshotFlushing);

	}

	@SneakyThrows
	@Test
	public void testMemInstanceLock() {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		MemInstanceLock memInstanceLock = new MemInstanceLock();
		List<Callable<Integer>> retryWriterThreads = new ArrayList<>();
		for (int i = 0; i < 1500; i++) {
			retryWriterThreads.add(() -> {
				memInstanceLock.add();
				return 0;
			});
		}
		pool.invokeAll(retryWriterThreads);
		pool.shutdown();
		System.out.printf("data-size:%s", memInstanceLock.getSize());
	}

	/**
	 * 一个实例对象，被多个线程并发访问，需要考虑线程安全性.
	 */
	private static class MemInstanceLock {

		private final ReentrantReadWriteLock updateLock = new ReentrantReadWriteLock();

		private int dataSize = 0;

		public void add() {
			updateLock.writeLock().lock();
			dataSize++;
			updateLock.writeLock().unlock();
		}

		public int getSize() {
			return dataSize;
		}

	}

	/**
	 * 测试带有重试功能的读写锁
	 */
	private static class RetryWriterThread implements Callable<Integer> {

		// 共享变量static，需要使用static的锁进行锁定
		private final static ReentrantReadWriteLock updateLock = new ReentrantReadWriteLock();

		private final static ReentrantLock reentrantLock = new ReentrantLock();

		private static HashMap<Integer, Integer> dic = new HashMap<>();

		private int k, v;

		public RetryWriterThread(int k, int v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public Integer call() throws Exception {
			int retries = 0;
			while (retries < 5) {
				try {
					// writeLock对头开放，对写锁定
					updateLock.writeLock().lock();
					dic.put(this.k, this.k / this.v);
					Thread.sleep(1000);
					System.out.println(new Date().toString() + Thread.currentThread().getId());
					break;
				}
				catch (Exception e) {
					// Memstore maybe full, so let's retry.
					retries++;
					try {
						System.err.println("RetryWriterThread try:" + retries);
						Thread.sleep(1000 * retries);

					}
					catch (InterruptedException e1) {
					}
				}
				finally {
					updateLock.writeLock().unlock();
				}
			}
			return 0;
		}

	}

}
