package com.lind.common.wheel;

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
public class SimpleTimeWheel {

	private final int tickMs; // 时间轮槽的时间间隔

	private final int wheelSize; // 时间轮槽的数量

	private final long startMs; // 时间轮启动时间

	private final List<List<Runnable>> timerTaskSlots; // 时间轮槽，存放任务

	private final ExecutorService executorService; // 用于执行任务的线程池

	private final Object[] lockArray; // 锁数组，用于保护每个槽的任务列表

	private int currentTickIndex; // 当前时间轮指针指向的槽索引

	public SimpleTimeWheel(int tickMs, int wheelSize) {
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
		// delayMs为多长时间后任务被执行
		// expireMs被计算为当前时间 startMs 加上 delayMs，表示任务的过期时间
		// currentMs表示当前时间
		// 如果 expireMs 小于当前时间 currentMs 加上时间轮的时间间隔
		// tickMs，则任务将被添加到当前时间轮槽（slot）中。这表示任务的执行时间已经过期，可以立即执行，因此将其添加到当前槽中。
		// 如果任务的执行时间在未来，就需要计算任务应该添加到哪个时间轮槽中
		// 1. 首先，计算需要跨越的时间轮圈数，即 (delayMs - tickMs) / tickMs，这表示任务需要等待多少个时间轮的刻度才能执行。
		// 2. 然后，计算任务应该添加到哪个槽中。这是通过将 (currentMs + delayMs) / tickMs
		// 加上跨越的时间轮圈数（totalTicks）并取模时间轮的大小（wheelSize）来完成。这告诉我们任务应该添加到哪个时间轮槽。
		long expireMs = startMs + delayMs;
		long currentMs = System.currentTimeMillis();

		if (expireMs < currentMs + tickMs) {
			// 将任务添加到当前时间轮槽中
			int currentIndex = (int) (((currentMs + delayMs) / tickMs) % wheelSize);
			timerTaskSlots.get(currentIndex).add(task);
		}
		else {
			// 计算需要跨越的时间轮针数和所在槽索引，例如槽数是8，我需要10秒后启动任务，就需要这个算法了
			int totalTicks = (int) ((delayMs - tickMs) / tickMs); // 例如延时20秒，相当于指针要走19个，(20_000-1000)/1000=19
			int currentIndex = (int) (((currentMs + delayMs) / tickMs + totalTicks) % wheelSize); // ((0+20000)/1000+19)%8=39%8=7
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
