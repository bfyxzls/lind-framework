package com.lind.common.queue;

import org.junit.Test;

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
				queue.put(message);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread consumer = new Thread(() -> {
			try {
				String message = queue.take();
				System.out.println("Consumer received message: " + message);
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
