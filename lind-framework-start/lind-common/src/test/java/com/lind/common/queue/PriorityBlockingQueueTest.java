package com.lind.common.queue;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue是一个可以按照优先级排序的阻塞队列，它的元素必须实现Comparable接口
 *
 * @author lind
 * @date 2023/5/22 11:13
 * @since 1.0.0
 */
public class PriorityBlockingQueueTest {

	public static void main(String[] args) throws InterruptedException {
		PriorityBlockingQueue<Student> queue = new PriorityBlockingQueue<>();

		queue.put(new Student("Tom", 76));
		queue.put(new Student("Jerry", 90));
		queue.put(new Student("Alice", 80));
		queue.put(new Student("Bob", 88));
		queue.put(new Student("Mike", 94));

		while (!queue.isEmpty()) {
			Student student = queue.take();
			System.out.println(student.toString());
		}
	}

	private static class Student implements Comparable<Student> {

		private String name;

		private int score;

		public Student(String name, int score) {
			this.name = name;
			this.score = score;
		}

		@Override
		public int compareTo(Student o) {
			return o.score - this.score;
		}

		@Override
		public String toString() {
			return "Student{" + "name='" + name + '\'' + ", score=" + score + '}';
		}

	}

}
