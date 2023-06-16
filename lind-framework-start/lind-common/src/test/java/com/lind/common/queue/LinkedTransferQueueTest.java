package com.lind.common.queue;

import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue 是一个无界队列，既支持先进先出（FIFO）的队列操作，也支持删除、检索和遍历等操作。特别的，它还支持试探性地添加元素和等待获取元素的
 * 操作
 *
 * @author lind
 * @date 2023/5/22 13:30
 * @since 1.0.0
 */
public class LinkedTransferQueueTest {

	public static void main(String[] args) throws InterruptedException {
		LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

		Thread producer = new Thread(() -> {
			try {
				String message = "Hello, World!";
				System.out.println("Producer is adding message: " + message);
				queue.transfer(message);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread consumer1 = new Thread(() -> {
			try {
				String message = queue.take();
				System.out.println("Consumer 1 received message: " + message);
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread consumer2 = new Thread(() -> {
			try {
				String message = queue.take();
				System.out.println("Consumer 2 received message: " + message);
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		producer.start();
		consumer1.start();
		consumer2.start();

		producer.join();
		consumer1.join();
		consumer2.join();
	}

}
