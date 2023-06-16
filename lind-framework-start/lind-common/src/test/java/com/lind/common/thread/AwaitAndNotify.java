package com.lind.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lind
 * @date 2023/5/22 10:21
 * @since 1.0.0
 */
@Slf4j
public class AwaitAndNotify {

	public static Object object = new Object();

	public static void main(String[] args) {
		Thread1 thread1 = new Thread1();
		Thread2 thread2 = new Thread2();

		thread1.start();

		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		thread2.start();
	}

	static class Thread1 extends Thread {

		@Override
		public void run() {
			synchronized (object) {
				try {
					log.info("线程" + Thread.currentThread().getName() + "开始被await了");

					object.wait();
				}
				catch (InterruptedException e) {
				}
				log.info("线程" + Thread.currentThread().getName() + "获取到了锁");
			}
		}

	}

	static class Thread2 extends Thread {

		@Override
		public void run() {
			synchronized (object) {
				object.notify();
				log.info("线程" + Thread.currentThread().getName() + "调用了object.notify()");
			}
			log.info("线程" + Thread.currentThread().getName() + "释放了锁");
		}

	}

}
