package com.lind.common.thread.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.backoff.Sleeper;

import java.util.Random;

/**
 * 用所学的tryLock知识，解决一个死锁问题：哲学家就餐问题 有五位哲学家，围坐在圆桌旁。 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
 * 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。 如果筷子被身边的人拿着，自己就得等待
 *
 * @author lind
 * @date 2023/5/29 10:33
 * @since 1.0.0
 */
@Slf4j
public class DeadLockTest {

	public static void main(String[] args) {
		/**
		 * 程序运行后一会儿会卡住。
		 *
		 * 使用我们之前的synchronized，就会出现死锁，即每个人某一时刻都只拥有一只筷子，又想要另外一只，此刻都无法得到另外一只，
		 * 彼此都又不放开自己拥有的筷子，这种现象就是死锁。
		 */
		Chopstick c1 = new Chopstick("1");
		Chopstick c2 = new Chopstick("2");
		Chopstick c3 = new Chopstick("3");
		Chopstick c4 = new Chopstick("4");
		Chopstick c5 = new Chopstick("5");
		new Philosopher("苏格拉底", c1, c2).start();
		new Philosopher("柏拉图", c2, c3).start();
		new Philosopher("亚里士多德", c3, c4).start();
		new Philosopher("赫拉克利特", c4, c5).start();
		new Philosopher("阿基米德", c5, c1).start();
	}

}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {

	Chopstick left;

	Chopstick right;

	public Philosopher(String name, Chopstick left, Chopstick right) {
		super(name);
		this.left = left;
		this.right = right;
	}

	@Override
	public void run() {
		while (true) {
			// 尝试获得左手筷子
			synchronized (left) {
				// 尝试获得右手筷子
				synchronized (right) {
					try {
						eat();
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	Random random = new Random();

	private void eat() throws InterruptedException {
		log.debug("eating...");
		Thread.sleep(1L);
	}

}

class Chopstick {

	String name;

	public Chopstick(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "筷子{" + name + '}';
	}

}
