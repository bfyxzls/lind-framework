package com.lind.common.thread.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.backoff.Sleeper;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 解决死锁的问题
 *
 * @author lind
 * @date 2023/5/29 10:51
 * @since 1.0.0
 */
public class DeadLockReentrantLock {

	public static void main(String[] args) {
		Chopstick2 c1 = new Chopstick2("1");
		Chopstick2 c2 = new Chopstick2("2");
		Chopstick2 c3 = new Chopstick2("3");
		Chopstick2 c4 = new Chopstick2("4");
		Chopstick2 c5 = new Chopstick2("5");
		new Philosopher2("苏格拉底", c1, c2).start();
		new Philosopher2("柏拉图", c2, c3).start();
		new Philosopher2("亚里士多德", c3, c4).start();
		new Philosopher2("赫拉克利特", c4, c5).start();
		new Philosopher2("阿基米德", c5, c1).start();
	}

}

@Slf4j(topic = "c.Philosopher")
class Philosopher2 extends Thread {

	Chopstick2 left;

	Chopstick2 right;

	public Philosopher2(String name, Chopstick2 left, Chopstick2 right) {
		super(name);
		this.left = left;
		this.right = right;
	}

	@Override
	public void run() {
		while (true) {
			if (left.tryLock()) {
				try {
					if (right.tryLock()) {
						try {
							eat();
						}
						finally {
							right.unlock();
						}

					}
				}
				finally {
					left.unlock();
				}
			}

		}
	}

	private void eat() {
		log.debug("eating...");
		try {
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}

class Chopstick2 extends ReentrantLock {

	String name;

	public Chopstick2(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "筷子{" + name + '}';
	}

}
