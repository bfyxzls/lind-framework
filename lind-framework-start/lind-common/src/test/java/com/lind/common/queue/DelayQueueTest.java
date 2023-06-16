package com.lind.common.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue是一个支持延时获取元素的无界阻塞队列。里面的元素全部都是“可延期”的元素，列头的元素是最先“到期”的元素，如果队列里面没有元素
 * 到期，是不能从列头获取元素的，哪怕有元素也不行。也就是说只有在延迟期到时才能够从队列中取元素
 *
 * @author lind
 * @date 2023/5/22 11:15
 * @since 1.0.0
 */
public class DelayQueueTest {

	public static void main(String[] args) throws InterruptedException {
		DelayQueue<DelayedElement> queue = new DelayQueue<>();

		queue.put(new DelayedElement("5-Tom", 5, TimeUnit.SECONDS));
		queue.put(new DelayedElement("2-Jerry", 2, TimeUnit.SECONDS));
		queue.put(new DelayedElement("3-Alice", 3, TimeUnit.SECONDS));
		queue.put(new DelayedElement("6-Bob", 6, TimeUnit.SECONDS));
		queue.put(new DelayedElement("4-Mike", 4, TimeUnit.SECONDS));

		while (!queue.isEmpty()) {
			DelayedElement element = queue.take();
			System.out.println(element.toString());
		}
	}

	private static class DelayedElement implements Delayed {

		private String name;

		private long delayTime;

		public DelayedElement(String name, long delay, TimeUnit unit) {
			this.name = name;
			this.delayTime = TimeUnit.MILLISECONDS.convert(delay, unit) + System.currentTimeMillis();
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long diff = delayTime - System.currentTimeMillis();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo(Delayed o) {
			return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
		}

		@Override
		public String toString() {
			return "DelayedElement{" + "name='" + name + '\'' + ", delayTime=" + delayTime + '}';
		}

	}

}
