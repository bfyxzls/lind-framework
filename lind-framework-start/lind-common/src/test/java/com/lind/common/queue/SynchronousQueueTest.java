package com.lind.common.queue;

import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 是一个阻塞队列，在添加元素时必须等待其他线程来获取该元素，而在获取元素时必须等待其他线程来添加该元素。
 * 这种情况下会发生阻塞，直到另一个线程操作队列。
 *
 * @author lind
 * @date 2023/5/22 13:25
 * @since 1.0.0
 */
public class SynchronousQueueTest {

	@Test
	public void producerAndConsumer() throws InterruptedException {
		SynchronousQueue<String> queue = new SynchronousQueue<>();

		Thread producer = new Thread(() -> {
			try {
				String message = "Hello, World!";
				System.out.println("Producer is adding message: " + message);
				for (int i = 0; i < 5; i++) {
					queue.put(message);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread consumer = new Thread(() -> {
			try {
				while (!queue.isEmpty()) {
					String message = queue.take();
					System.out.println("Consumer received message: " + message);
					Thread.sleep(2000);// 消费的比较慢，也影响了生产的速度

				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		producer.start();
		consumer.start();

		producer.join();
		consumer.join();
	}

}
