package com.lind.common.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * add方法在添加元素时，若超出了度列的长度会直接抛出异常： put方法在添加元素时，若超出了度列的长度会一直阻塞等待：
 * offer方法在添加元素时，若超出了度列的长度会直接返回false： offer(E e, long timeout,
 * TimeUnitunit)：可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。
 * poll：移除并返问队列头部的元素，若队列为空，则返回null。 take：移除并返回队列头部的元素，若队列为空，则阻塞等待。 poll(long
 * timeout,TimeUnit unit)：移除并返回队列头部的元素，若队列为空，则等待指定的时间，如果还没数据，就返回null。
 */
public class ArrayBlockingQueueTest {

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		Thread producerThread = new Thread(producer);
		Thread consumerThread = new Thread(consumer);

		producerThread.start();
		consumerThread.start();

		producerThread.join();
		consumerThread.join();
	}

	private static class Producer implements Runnable {

		private final ArrayBlockingQueue<Integer> queue;

		private final Random random;

		public Producer(ArrayBlockingQueue<Integer> queue) {
			this.queue = queue;
			this.random = new Random();
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					System.out.println("Producer inserting: " + i);
					// 队列满时，offer返回false，如果使用add方式，直接抛IllegalStateException
					while (!queue.offer(i)) {
						System.out.println("Queue full, waiting for consumer to take...");
						Thread.sleep(1000);
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static class Consumer implements Runnable {

		private final ArrayBlockingQueue<Integer> queue;

		public Consumer(ArrayBlockingQueue<Integer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 5; i++) {
					System.out.println("Consumer taking: " + queue.take());
				}
				while (!queue.isEmpty()) {
					System.out.println("Consumer taking: " + queue.take());
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
