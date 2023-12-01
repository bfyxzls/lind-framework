package com.lind.common.wheel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2023/6/8 17:27
 * @since 1.0.0
 */
@Slf4j
public class SimpleTimeWheelTest {

	// 示例使用
	public static void main(String[] args) throws InterruptedException {
		log.info("任务开始");
		// 每秒一个槽，总共10个槽,槽太少相当于时间延时支持的就小【最大支持10S的延时】,如果延时时间太大，这些任务并不会被执行
		SimpleTimeWheel timeWheel = new SimpleTimeWheel(1000, 8);
		timeWheel.addTask(() -> log.info("Task 1 executed"), 5000); // 5秒后执行任务1
		timeWheel.addTask(() -> log.info("Task 2 executed"), 10000); // 10秒后执行任务2
		timeWheel.addTask(() -> log.info("Task 3 executed"), 20000); // 20秒后执行任务3

		TimeUnit.MINUTES.sleep(1);
		log.info("任务结束");
		timeWheel.stop();
		// 显式关闭线程池并等待任务执行完成
		timeWheel.awaitTermination();
	}

	/**
	 * 自己搞点事情出来.
	 */
	@Test
	public void self() {
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int wheelSize = 512;
		int tickMs = 1000;
		long startMs = System.currentTimeMillis(); // 时间轮启动时间
		long currentMs = System.currentTimeMillis();// 添加任务时的时间
		// int delayMs = 3000; // 任务延时时间
		// long expireMs = startMs + delayMs; // 任务到期时间
		List<List<Runnable>> timerTaskSlots = new ArrayList<>(wheelSize);// 时间轮槽，存放任务
		for (int i = 0; i < wheelSize; i++) {
			timerTaskSlots.add(Collections.synchronizedList(new LinkedList<>()));
		}

		int index = (int) ((3000 + currentMs) / tickMs) % wheelSize;// 当前任务在轮盘中的位置,添加任务到盘子
		timerTaskSlots.get(index).add(() -> {
			log.info("hello1");
		});

		int index2 = (int) ((5000 + currentMs) / tickMs) % wheelSize;// 当前任务在轮盘中的位置,添加任务到盘子
		timerTaskSlots.get(index2).add(() -> {
			log.info("hello10");
		});

		int index3 = (int) ((8000 + currentMs) / tickMs) % wheelSize;// 当前任务在轮盘中的位置,添加任务到盘子
		timerTaskSlots.get(index3).add(() -> {
			log.info("hello8");
		});
		int currentTickIndex = 0; // 当前时间轮指针指向的槽索引

		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(tickMs);

				int nextTickIndex = (currentTickIndex + 1) % wheelSize;
				List<Runnable> tasksToExecute = new ArrayList<>();
				tasksToExecute.addAll(timerTaskSlots.get(nextTickIndex));
				timerTaskSlots.get(nextTickIndex).clear();

				for (Runnable task : tasksToExecute) {
					executorService.execute(task);
				}
				currentTickIndex = nextTickIndex;
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}

	}

	@Test
	public void incMod() {
		for (int i = 1000; i < 1010; i++) {
			System.out.println("i mod 10=" + i % 10);
		}
	}

	@Test
	public void cpu() {
		log.info(String.valueOf(Runtime.getRuntime().availableProcessors()));
	}

}
