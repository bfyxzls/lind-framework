package com.lind.common.thread.forkjointask;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;

/**
 * 此示例程序利用一个执行器来计算多个长整型的和。内部 Sum 类实现了 Callable 接口，执行器将该接口用于结果计算，并发工作是在 call()
 * 方法内部执行的。java.util.concurrent.Executors 类提供了一些实用方法，如提供预配置的执行器或将普通的旧 java.lang.Runnable
 * 对象包装到 Callable 的实例中。与 Runnable 相比，使用 Callable 的优势是可以显式返回一个值。
 * 此示例使用了一个执行器向两个线程分派工作。ExecutorService.invokeAll() 方法接收 Callable
 * 实例的集合，并在返回之前等待所有实例的完成。它返回一个 Future 对象的列表，所有这些对象都代表计算的“未来”结果。如果以异步方式工作，可以测试每个 Future
 * 对象，检查其对应的 Callable
 * 是否已经完成工作，并检查它是否抛出了一个异常，甚至可以将其取消。相比之下，使用普通传统线程时，必须通过一个共享的可变布尔值来编码取消逻辑，并通过定期检查这个布尔值来废除代码。由于
 * invokeAll() 导致阻塞，我们可以直接遍历 Future 实例并获取其计算和。 还需注意的是，必须关闭执行器服务。如果未关闭，当主方法退出时，Java
 * 虚拟机将不会退出，因为环境中仍有活跃的线程。
 */
public class CallableTest {

	public static <T> List<List<T>> splitList(List<T> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<T>> result = new ArrayList<List<T>>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

	@Test
	public void test() throws Exception {

		// 开启二个线程并发做，如果你有3个任务，将会出现排除
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<Future<Long>> results = executor
				.invokeAll(asList(new Sum(0, 10), new Sum(100, 1_000), new Sum(10_000, 1_000_000)));
		executor.shutdown();

		// 合并结果
		for (Future<Long> result : results) {
			System.out.println(result.get());
		}
	}

	@Test
	public void testList() throws Exception {

		// 开启二个线程并发做，如果你有3个任务，将会出现排除
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<UpperCaseList> upperCaseLists = new ArrayList<>();
		List<String> list = asList("a", "b", "c", "d", "e", "f");
		splitList(list, 2).forEach(o -> upperCaseLists.add(new UpperCaseList(o)));
		List<Future<TaskResult>> results = executor.invokeAll(upperCaseLists);
		executor.shutdown();

		// 合并结果
		for (Future<TaskResult> result : results) {
			System.out.println(result.get());
		}
	}

	static class Sum implements Callable<Long> {

		private final long from;

		private final long to;

		Sum(long from, long to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public Long call() throws InterruptedException {
			long acc = 0;
			for (long i = from; i <= to; i++) {
				acc = acc + i;
			}
			System.out.println("并发执行:" + Thread.currentThread().getId());
			return acc;
		}

	}

	static class UpperCaseList implements Callable<TaskResult> {

		/**
		 * 原数据
		 * @param list
		 */
		private List<String> list;

		private List<String> successList = new ArrayList<>();

		private List<String> failList = new ArrayList<>();

		public UpperCaseList(List<String> list) {
			this.list = list;
		}

		@Override
		public TaskResult call() throws Exception {
			System.out.println("并发执行:" + Thread.currentThread().getId());
			for (int i = 0; i < list.size(); i++) {
				if (i % 2 == 0)
					successList.add(list.get(i));
				else
					failList.add(list.get(i));
			}
			return new TaskResult(successList, failList);
		}

	}

	@Data
	@AllArgsConstructor
	static class TaskResult {

		private List<String> successList;

		private List<String> failList;

	}

}
