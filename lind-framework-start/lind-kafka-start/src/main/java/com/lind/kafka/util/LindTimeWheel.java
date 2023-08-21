package com.lind.kafka.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Lind时间轮.
 *
 * @author lind
 * @date 2023/6/8 17:27
 * @since 1.0.0
 */
public class LindTimeWheel {

	private final int tickMs; // 时间轮槽的时间间隔

	private final int wheelSize; // 时间轮槽的数量

	private final long startMs; // 时间轮启动时间

	private final List<List<Runnable>> timerTaskSlots; // 时间轮槽，存放任务

	private final ExecutorService executorService; // 用于执行任务的线程池

	private final Object[] lockArray; // 锁数组，用于保护每个槽的任务列表

	private int currentTickIndex; // 当前时间轮指针指向的槽索引

	public LindTimeWheel(int tickMs, int wheelSize) {
		this.tickMs = tickMs;
		this.wheelSize = wheelSize;
		this.startMs = System.currentTimeMillis();
		this.timerTaskSlots = new ArrayList<>(wheelSize);
		for (int i = 0; i < wheelSize; i++) {
			timerTaskSlots.add(Collections.synchronizedList(new LinkedList<>()));
		}
		this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.lockArray = new Object[wheelSize];
		for (int i = 0; i < wheelSize; i++) {
			lockArray[i] = new Object();
		}
		initialize();
	}

	/**
	 * 停止时间轮
	 */
	public void awaitTermination() {
		try {
			// 这样，当时间轮停止时，它将首先关闭线程池，并等待最多10秒钟的时间，直到所有任务完成执行。这样就可以避免线程池被提前关闭而导致的拒绝执行错误。
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		// 启动时间轮指针移动的线程
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(tickMs);
					moveNextTick();
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
		thread.start();
	}

	public void addTask(Runnable task, long delayMs) {
		long expireMs = startMs + delayMs;
		long currentMs = System.currentTimeMillis();

		if (expireMs < currentMs + tickMs) {
			// 将任务添加到当前时间轮槽中
			int currentIndex = (int) (((currentMs + delayMs) / tickMs) % wheelSize);
			timerTaskSlots.get(currentIndex).add(task);
		}
		else {
			// 计算需要跨越的时间轮圈数和所在槽索引
			int totalTicks = (int) ((delayMs - tickMs) / tickMs);
			int currentIndex = (int) (((currentMs + delayMs) / tickMs + totalTicks) % wheelSize);
			timerTaskSlots.get(currentIndex).add(() -> {
				long current = System.currentTimeMillis();
				if (current >= expireMs) {
					task.run();
				}
			});
		}
	}

	private void moveNextTick() {
		int nextTickIndex = (currentTickIndex + 1) % wheelSize;
		List<Runnable> tasksToExecute = new ArrayList<>();
		synchronized (lockArray[nextTickIndex]) {
			tasksToExecute.addAll(timerTaskSlots.get(nextTickIndex));
			timerTaskSlots.get(nextTickIndex).clear();
		}
		for (Runnable task : tasksToExecute) {
			executorService.execute(task);
		}
		currentTickIndex = nextTickIndex;
	}

	public void stop() {
		executorService.shutdown();
	}

}
