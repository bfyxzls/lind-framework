package com.lind.common.thread.forkjointask;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ForkJoinTaskTest {

	static Logger logger = LoggerFactory.getLogger(ForkJoinTaskTest.class);

	@SneakyThrows
	public static void main(String[] args) {
		Long startNum = 1l;
		Long endNum = 1000000000l;

		long startTime = System.currentTimeMillis();

		CountTask countTask = new CountTask(startNum, endNum);
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		Future<Long> result = forkJoinPool.submit(countTask);
		try {
			System.out.println("结果--》result:" + result.get());
			long endTime = System.currentTimeMillis();
			System.out.println("costTime:" + (endTime - startTime));
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 大任务拆分,拆分成一个一个小任务，不是小集合. parallelism:可并行数量
	 */
	@Test
	public void bigTaskFork() {
		List<Integer> maps = Lists.newArrayList();
		int count = 100;
		for (int i = 0; i < count; i++) {
			maps.add(i);
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<Integer> odds = new ArrayList<>();
		ForkJoinPool forkJoinPool = new ForkJoinPool(10);
		forkJoinPool.submit(() -> {
			maps.parallelStream().forEach(map -> {
				if (map % 2 == 0) {
					odds.add(map);
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});

		}).join();
		stopWatch.stop();
		logger.info(stopWatch.prettyPrint());

		// fork集合
		List<List<Integer>> jihe = new ArrayList<>();
		jihe = ListUtil.split(maps, 9);
		List<List<Integer>> finalJihe = jihe;
		forkJoinPool.submit(() -> {
			finalJihe.parallelStream().forEach(map -> {
				logger.info("map size:{}", map.size());
			});
		}).join();
	}

	@Test
	public void demo() {
		List<Integer> maps = Lists.newArrayList();
		int count = 100;
		for (int i = 0; i < count; i++) {
			maps.add(i);
		}
		List<Integer> successList = Lists.newArrayList();
		ForkJoinPool forkJoinPool = new ForkJoinPool(10);
		try {
			forkJoinPool.submit(() -> maps.parallelStream().forEach(map -> {
				try {
					if (successList.contains(map)) {
						System.out.println("重复了：" + map);
					}
					successList.add(map);
				}
				catch (Exception e) {

				}
			})).join();
		}
		catch (Exception e) {

		}
		if (count != successList.size()) {
			System.out.println(successList.size());
			maps.stream().filter(t -> !successList.contains(t)).collect(Collectors.toList())
					.forEach(System.out::println);

		}
	}

	public static class CountTask extends RecursiveTask<Long> {

		Long maxCountRange = 100000000l;// 最大计算范围

		Long startNum, endNum;

		public CountTask(Long startNum, Long endNum) {
			this.startNum = startNum;
			this.endNum = endNum;
		}

		@Override
		protected Long compute() {
			long range = endNum - startNum;
			long sum = 0;
			if (range >= maxCountRange) {// 如果这次计算的范围大于了计算时规定的最大范围，则进行拆分
				// 每次拆分时，都拆分成原来任务范围的一半
				// 如1-10,则拆分为1-5,6-10
				Long middle = (startNum + endNum) / 2;
				CountTask subTask1 = new CountTask(startNum, middle);
				CountTask subTask2 = new CountTask(middle + 1, endNum);
				// 拆分后，执行fork
				subTask1.fork();
				subTask2.fork();

				sum += subTask2.join();
				sum += subTask1.join();
			}
			else {// 在范围内，则进行计算
				for (; startNum <= endNum; startNum++) {
					sum += startNum;
				}
			}
			return sum;
		}

	}

}
