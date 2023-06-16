package com.lind.common.thread.forkjointask;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 使用forkJoinPool将100个字符转成大写，并且10个线程并行执行.
 *
 * @author lind
 * @date 2023/6/2 15:04
 * @since 1.0.0
 */
@Slf4j
public class CurrentUserTest extends RecursiveTask<List<String>> {

	private static final int THRESHOLD = 5; // 我的cpu是5核心的，要执行100个任务，阈值50个为2个任务并行，30为4个任务并行，10为10个任务并行，这个并行的分割可按着CPU的核数设置。

	private List<String> list;

	private int start;

	private int end;

	public CurrentUserTest(List<String> list, int start, int end) {
		this.list = list;
		this.start = start;
		this.end = end;
	}

	@SneakyThrows
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add("String_");
		}

		ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		CurrentUserTest uppercaseTask = new CurrentUserTest(list, 0, list.size() - 1);
		List<String> results = forkJoinPool.invoke(uppercaseTask);
		// 线程阻塞，等待所有任务完成
		forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
		forkJoinPool.shutdown();
		for (String str : results) {
			System.out.println(str);
		}

	}

	@SneakyThrows
	@Override
	protected List<String> compute() {
		if (end - start <= THRESHOLD) {
			// 核心逻辑，在else代码段将大任务拆分后，核心逻辑在这里执行
			List<String> results = new ArrayList<>(end - start + 1);
			for (int i = start; i <= end; i++) {
				String str = list.get(i).toUpperCase() + "_" + Thread.currentThread().getId();
				Thread.sleep(1000);// 有个延时，方便查看并行效果
				results.add(str);
			}
			return results;
		}
		else {
			int mid = start + (end - start) / 2;
			CurrentUserTest leftTask = new CurrentUserTest(list, start, mid);
			CurrentUserTest rightTask = new CurrentUserTest(list, mid + 1, end);
			leftTask.fork();
			rightTask.fork();
			List<String> leftResult = leftTask.join();
			List<String> rightResult = rightTask.join();
			leftResult.addAll(rightResult);
			return leftResult;
		}
	}

}
