package com.lind.common.thread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * ForkJoinPool线程池最大的特点就是分叉(fork)合并(join).
 */
public class ForkJoinTest {

	private static ForkJoinPool forkJoinPool;

	/**
	 * 分解求和
	 */
	@Test
	public void sum() {
		// 也可以jdk8提供的通用线程池ForkJoinPool.commonPool
		// 可以在构造函数内指定线程数
		forkJoinPool = new ForkJoinPool();
		long[] numbers = LongStream.rangeClosed(1, 1000).toArray();
		// 这里可以调用submit方法返回的future，通过future.get获取结果
		Long result = forkJoinPool.invoke(new SumTask(numbers, 0, numbers.length - 1));
		forkJoinPool.shutdown();
		System.out.println("最终结果：" + result);
		System.out.println("活跃线程数：" + forkJoinPool.getActiveThreadCount());
		System.out.println("窃取任务数：" + forkJoinPool.getStealCount());
	}

	/**
	 * 求和任务类继承RecursiveTask ForkJoinTask一共有3个实现： RecursiveTask：有返回值 RecursiveAction：无返回值
	 * CountedCompleter：无返回值任务，完成任务后可以触发回调
	 */
	private static class SumTask extends RecursiveTask<Long> {

		private long[] numbers;

		private int from;

		private int to;

		public SumTask(long[] numbers, int from, int to) {
			this.numbers = numbers;
			this.from = from;
			this.to = to;
		}

		/**
		 * ForkJoin执行任务的核心方法
		 * @return
		 */
		@Override
		protected Long compute() {
			if (to - from < 10) { // 设置拆分的最细粒度，即阈值，如果满足条件就不再拆分，执行计算任务
				long total = 0;
				for (int i = from; i <= to; i++) {
					total += numbers[i];
				}
				return total;
			}
			else { // 否则继续拆分，递归调用
				int middle = (from + to) / 2;
				SumTask taskLeft = new SumTask(numbers, from, middle);
				SumTask taskRight = new SumTask(numbers, middle + 1, to);
				taskLeft.fork();
				taskRight.fork();
				return taskLeft.join() + taskRight.join();
			}
		}

	}

	/**
	 * 注意点： parallelStream并行流一定要使用线程安全的对象，比如有这样的一个场景ArrayList，因为它是线程不安全的，所以有可能会溢出
	 * 此时，要么使用线程安全的容器，比如Vector，要么使用collect完成串行收集。
	 */
	@Test
	public void notice() {
		List<Integer> list = new ArrayList<>();
		IntStream.rangeClosed(1, 10000).parallel().forEach(i -> list.add(i));
	}

	@Test
	public void notice_true() {
		List<Integer> collect = IntStream.rangeClosed(1, 10000).parallel().boxed().collect(Collectors.toList());
	}

}
