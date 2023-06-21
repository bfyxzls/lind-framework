package com.lind.common.queue;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * LinkedBlockingDeque 是一个阻塞式双端队列，支持在队列的两端进行添加和删除元素的操作，而且线程安全。它还提供了一些其他的方法， 如
 * peekFirst、peekLast、pollFirst、pollLast 等，用于查看元素和删除元素
 *
 * @author lind
 * @date 2023/5/22 13:32
 * @since 1.0.0
 */
public class LinkedBlockingDequeTest {

	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>(1);

		Thread producer = new Thread(() -> {
			try {
				deque.putFirst("Hello");
				System.out.println("Producer added message: Hello");
				deque.putLast("World");
				System.out.println("Producer added message: World");
				deque.putLast("!");
				System.out.println("Producer added message: !");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread consumer = new Thread(() -> {
			try {
				String message1 = deque.takeFirst();
				System.out.println("Consumer received message: " + message1);
				String message2 = deque.takeLast();
				System.out.println("Consumer received message: " + message2);
				String message3 = deque.takeLast();
				System.out.println("Consumer received message: " + message3);
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
