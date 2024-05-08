package com.lind.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * wait和notify要在synchronized里.
 */
@Slf4j
public class ThreadWaitNotify {

	@Test
	public void test() throws InterruptedException {
		// 仓库对象
		AbstractStorage abstractStorage = new Storage1();

		// 生产者对象
		Producer p1 = new Producer(abstractStorage);
		Producer p2 = new Producer(abstractStorage);
		Producer p3 = new Producer(abstractStorage);

		// 消费者对象
		Consumer c1 = new Consumer(abstractStorage);
		Consumer c2 = new Consumer(abstractStorage);
		Consumer c3 = new Consumer(abstractStorage);

		// 设置生产者产品生产数量
		p1.setNum(10);
		p2.setNum(10);
		p3.setNum(110);

		// 设置消费者产品消费数量
		c1.setNum(50);
		c2.setNum(20);
		c3.setNum(30);

		// 线程开始执行
		c1.start();
		c2.start();
		c3.start();

		p1.start();
		p2.start();
		p3.start();

		Thread.sleep(1000);
	}

	@Test
	public void nanos() throws InterruptedException {
		long deadline = System.nanoTime() + 1000;
		long sleepTimeNanos = deadline - System.nanoTime();
		log.info("启动");
		TimeUnit.NANOSECONDS.sleep(sleepTimeNanos);
		log.info("再启动");

	}

	public interface AbstractStorage {

		void consume(int num);

		void produce(int num);

	}

	/**
	 * 生产者和消费者的问题 1、生产者产生资源往池子里添加，前提是池子没有满，如果池子满了，则生产者暂停生产，直到自己的生成能放下池子。
	 * 2、消费者消耗池子里的资源，前提是池子的资源不为空，否则消费者暂停消耗，进入等待直到池子里有资源数满足自己的需求。 wait、notify/notifyAll()
	 * 实现
	 */
	public static class Storage1 implements AbstractStorage {

		// 仓库最大容量
		private final int MAX_SIZE = 100;

		// 仓库存储的载体
		private LinkedList list = new LinkedList();

		// 生产产品
		public void produce(int num) {
			// 同步
			synchronized (list) {
				// 仓库剩余的容量不足以存放即将要生产的数量，暂停生产
				while (list.size() + num > MAX_SIZE) {
					System.err.println("【要生产的产品数量】:" + num + "\t【库存量】:" + list.size() + "\t暂时不能执行生产任务!");

					try {
						// 条件不满足，生产阻塞
						list.wait();
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				for (int i = 0; i < num; i++) {
					list.add(new Object());
				}

				System.out.println("【生产产品数】:" + num + "\t【现仓储量为】:" + list.size());

				list.notifyAll();
			}
		}

		// 消费产品
		public void consume(int num) {
			synchronized (list) {
				// 不满足消费条件
				while (num > list.size()) {
					System.err.println("【要消费的产品数量】:" + num + "\t【库存量】:" + list.size() + "\t暂时不能执行生产任务!");

					try {
						list.wait();
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// 消费条件满足，开始消费
				for (int i = 0; i < num; i++) {
					list.remove();
				}

				System.out.println("【消费产品数】:" + num + "\t【现仓储量为】:" + list.size());

				list.notifyAll();
			}
		}

	}

	public static class Producer extends Thread {

		// 所属的仓库
		public AbstractStorage abstractStorage;

		// 每次生产的数量
		private int num;

		public Producer(AbstractStorage abstractStorage) {
			this.abstractStorage = abstractStorage;
		}

		public void setNum(int num) {
			this.num = num;
		}

		// 线程run函数
		@Override
		public void run() {
			produce(num);
		}

		// 调用仓库Storage的生产函数
		public void produce(int num) {
			abstractStorage.produce(num);
		}

	}

	public static class Consumer extends Thread {

		// 每次消费的产品数量
		private int num;

		// 所在放置的仓库
		private AbstractStorage abstractStorage1;

		// 构造函数，设置仓库
		public Consumer(AbstractStorage abstractStorage1) {
			this.abstractStorage1 = abstractStorage1;
		}

		// 线程run函数
		public void run() {
			consume(num);
		}

		// 调用仓库Storage的生产函数
		public void consume(int num) {
			abstractStorage1.consume(num);
		}

		public void setNum(int num) {
			this.num = num;
		}

	}

}
