package com.lind.common.thread.lock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	private final ExecutorService exec;

	private final Semaphore semaphore;

	public SemaphoreTest(int nThread) {
		exec = Executors.newFixedThreadPool(nThread);
		semaphore = new Semaphore(nThread);
	}

	private final class Man implements Runnable {

		private int id;

		public Man(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire(); // 获取许可
				Thread.sleep(1000);
				System.out.println("ID：" + id + "在此房间,剩余许可：" + semaphore.availablePermits());
			}
			catch (InterruptedException e) {
			}
			finally {
				semaphore.release(); // 释放许可
				System.out.println("ID：" + id + "走出房间,剩余许可：" + semaphore.availablePermits());
			}
		}

	}

	public void init() throws InterruptedException, ExecutionException {
		for (int i = 1; i <= 8; i++) // 一共8个人想进入房间
			exec.submit(new Man(i), true);
		exec.shutdown();
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SemaphoreTest task = new SemaphoreTest(4); // 设置线程数，相当于房间最大能容纳4个人
		task.init();
	}

}
