package com.lind.common.thread.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

	static final Lock lock = new ReentrantLock(true);
	static final Lock noLock = new ReentrantLock(false);

	/**
	 * 容量范围,默认值为 Integer.MAX_VALUE
	 */
	private final int capacity = Integer.MAX_VALUE;

	/**
	 * 当前队列中元素数量
	 */
	private final AtomicInteger count = new AtomicInteger(0);

	/**
	 * take, poll等方法的锁
	 */
	private final ReentrantLock takeLock = new ReentrantLock();

	/**
	 * 获取队列的 Condition（条件）实例
	 */
	private final Condition notEmpty = takeLock.newCondition();

	/**
	 * put, offer等方法的锁
	 */
	private final ReentrantLock putLock = new ReentrantLock();

	/**
	 * 插入队列的 Condition（条件）实例
	 */
	private final Condition notFull = putLock.newCondition();

	/**
	 * 公平锁. 三个线程两次获取到锁的顺序是相同的.
	 */
	@Test
	public void fairnessLockTest() throws InterruptedException {
		new Thread(this::fairnessLock, "A").start();
		new Thread(this::fairnessLock, "B").start();
		new Thread(this::fairnessLock, "C").start();

		TimeUnit.SECONDS.sleep(6);
	}

	/**
	 * 不公平锁. 三个线程两次获取到锁的顺序不同.
	 */
	@Test
	public void noFairnessLockTest() throws InterruptedException {
		new Thread(this::noFairnessLock, "A").start();
		new Thread(this::noFairnessLock, "B").start();
		new Thread(this::noFairnessLock, "C").start();

		TimeUnit.SECONDS.sleep(6);
	}

	/**
	 * 响应中断,出现死索时，使用interrupt()之后可以获取到锁.
	 */
	@Test
	public void interruptLockTest() throws InterruptedException {
		Thread thread1 = new Thread(new InterruptLock(lock, noLock));
		Thread thread2 = new Thread(new InterruptLock(noLock, lock));
		thread1.start();
		thread2.start();
		thread1.interrupt();// 第一个线程中段
		TimeUnit.SECONDS.sleep(5);
	}

	@Test
	public void tryLockTest() throws InterruptedException {
		Thread thread1 = new Thread(new InterruptLock(lock, noLock));
		Thread thread2 = new Thread(new InterruptLock(noLock, lock));
		thread1.start();
		thread2.start();
		thread1.interrupt();// 第一个线程中段
		TimeUnit.SECONDS.sleep(5);
	}

	void noFairnessLock() {
		for (int i = 0; i < 2; i++) {
			try {
				noLock.lock();
				System.out.println(Thread.currentThread().getName() + "获取了锁");
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				noLock.unlock();
			}
		}
	}

	void fairnessLock() {
		for (int i = 0; i < 2; i++) {
			try {
				lock.lock();
				System.out.println(Thread.currentThread().getName() + "获取了锁");
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				lock.unlock();
			}
		}
	}

	/**
	 * Signals a waiting take. Called only from put/offer (which do not otherwise
	 * ordinarily lock takeLock.)
	 */
	private void signalNotEmpty() {
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			notEmpty.signal();
		}
		finally {
			takeLock.unlock();
		}
	}

	/**
	 * 将指定元素插入到此队列的尾部，如有必要，则等待空间变得可用
	 */
	public void put(String e) throws InterruptedException {
		// 判断添加元素是否为null
		if (e == null)
			throw new NullPointerException();
		int c = -1;

		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		// 获取插入的可中断锁
		putLock.lockInterruptibly();
		try {
			try {
				// 判断队列是否已满
				while (count.get() == capacity)
					// 如果已满则阻塞添加线程
					notFull.await();
			}
			catch (InterruptedException ie) {
				// 失败就唤醒添加线程
				notFull.signal();
				throw ie;
			}
			// 添加元素
			insert(e);
			// 修改c值
			c = count.getAndIncrement();
			// 根据c值判断队列是否已满
			if (c + 1 < capacity)
				// 未满则唤醒添加线程
				notFull.signal();
		}
		finally {
			// 释放锁
			putLock.unlock();
		}
		// c等于0代表添加成功
		if (c == 0)
			signalNotEmpty();
	}

	void insert(String e) {

	}

	static class InterruptLock implements Runnable {

		Lock lock1;

		Lock lock2;

		public InterruptLock(Lock lock1, Lock lock2) {
			this.lock1 = lock1;
			this.lock2 = lock2;
		}

		@Override
		public void run() {
			// lock 优先考虑获取锁，待获取锁成功后，才响应中断。
			// lockInterruptibly 优先考虑响应中断，而不是响应锁的普通获取或重入获取。

			try {
				lock1.lockInterruptibly();
				TimeUnit.MILLISECONDS.sleep(50);
				lock2.lockInterruptibly();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				lock1.unlock();
				lock2.unlock();
				System.out.println("正常结束，获取到了锁");
			}
		}

	}

	static class tryLockClass implements Runnable {

		Lock lock1;

		Lock lock2;

		public tryLockClass(Lock lock1, Lock lock2) {
			this.lock1 = lock1;
			this.lock2 = lock2;
		}

		@Override
		public void run() {
			// 设置tryLock的超时等待时间tryLock(long timeout,TimeUnit unit),
			// 也就是说一个线程在指定的时间内没有获取锁，那就会返回false，就可以再去做其他事了
			try {
				if (!lock1.tryLock(10, TimeUnit.SECONDS)) {
					TimeUnit.MILLISECONDS.sleep(50);
				}
				if (!lock2.tryLock()) {
					lock2.lockInterruptibly();
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				lock1.unlock();
				lock2.unlock();
				System.out.println("正常结束，获取到了锁");
			}
		}

	}

}
