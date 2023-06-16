package com.lind.common.thread.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lind
 * @date 2023/5/29 10:26
 * @since 1.0.0
 */
@Slf4j
public class ReentrantLockTest {

	static ReentrantLock reentrantLock = new ReentrantLock();

	public static void main(String[] args) {

		Thread t1 = new Thread(() -> {
			log.debug("t1尝试获取锁");
			try {
				if (!reentrantLock.tryLock(2, TimeUnit.SECONDS)) {
					log.debug("没有获取到锁");
					return;
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				log.debug("t1 获取到锁");
			}
			finally {
				reentrantLock.unlock();
			}

		}, "t1");

		// 主线程上锁
		reentrantLock.lock();
		log.debug("main 获取到锁");
		t1.start();

		try {
			Thread.sleep(1000);// 小于锁的超时时间2秒，t1就可以获取到锁
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			reentrantLock.unlock();
		}
	}

}
