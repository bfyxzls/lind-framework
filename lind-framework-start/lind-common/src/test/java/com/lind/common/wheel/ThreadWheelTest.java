package com.lind.common.wheel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * lind时间轮测试，添加了Work类型，支持更大的时间延时，当任务没有到期时，再把任务放回timerTaskSlots
 *
 * @author lind
 * @date 2023/11/30 15:55
 * @since 1.0.0
 */
public class ThreadWheelTest {

	static Logger logger = LoggerFactory.getLogger(ThreadWheelTest.class);

	private final int tickMs = 1000; // 时间轮槽的时间间隔

	private final int wheelSize = 8; // 时间轮槽的数量

	private final long startMs; // 时间轮启动时间

	private final List<List<WorkTask>> timerTaskSlots; // 时间轮槽，存放任务

	private final Object[] lockArray; // 锁数组，用于保护每个槽的任务列表

	private final ExecutorService executorService; // 用于执行任务的线程池

	private int currentTickIndex; // 当前时间轮指针指向的槽索引，可能比wheelSize大，每隔tickMs就会加1

	public ThreadWheelTest() {
		startMs = System.currentTimeMillis();
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

	public static void main(String[] args) throws InterruptedException {
		ThreadWheelTest threadWheelTest = new ThreadWheelTest();
		threadWheelTest.addTask(() -> {
			logger.info("hello world 15000");
		}, 30000);
		TimeUnit.MILLISECONDS.sleep(60000);
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

	// 不断的循序渐进
	private void moveNextTick() {
		int nextTickIndex = (currentTickIndex + 1) % wheelSize; // nextTickIndex是个循环变量，它小于等于wheelSize
		List<WorkTask> tasksToExecute = new ArrayList<>();

		synchronized (lockArray[nextTickIndex]) {
			tasksToExecute.addAll(timerTaskSlots.get(nextTickIndex)); // 会把与nextTickIndex对应的槽中的任务全部取出来，放到tasksToExecute中，最后把nextTickIndex赋给currentTickIndex，周而复始
			timerTaskSlots.get(nextTickIndex).clear();
		}
		logger.info("moveNextTick currentTickIndex:{},task count{}", currentTickIndex, tasksToExecute.size());

		for (WorkTask task : tasksToExecute) {

			if (!task.isRun) { // 如果本轮任务没有执行，说明是个周期性任务，需要重新添加到时间轮中
				timerTaskSlots.get(nextTickIndex).add(task);
			}
			executorService.execute(task.runnable);

		}

		currentTickIndex = nextTickIndex;
	}

	public void addTask(Runnable task, long delayMs) {
		long expireMs = startMs + delayMs;
		long currentMs = System.currentTimeMillis();

		if (expireMs < currentMs + tickMs) {
			// 将任务添加到当前时间轮槽中
			int currentIndex = (int) (((currentMs + delayMs) / tickMs) % wheelSize);
			timerTaskSlots.get(currentIndex).add(new WorkTask(true, task));
		}
		else {
			// 计算时间轮指针要跨越的槽数，如果是60秒的任务，速度是1S走一个槽，那么就要跨越（60-1）/1=59个槽
			int totalTicks = (int) ((delayMs - tickMs) / tickMs);
			// currentIndex是个循环变量，它小于等于wheelSize
			// 如果currentMs=0,delayMs=60s,tickMs=1s,totalTicks=59，最后落在7号槽上
			int currentIndex = (int) (((currentMs + delayMs) / tickMs + totalTicks) % wheelSize);
			timerTaskSlots.get(currentIndex).add(new WorkTask(System.currentTimeMillis() >= expireMs, () -> {
				if (System.currentTimeMillis() >= expireMs) {
					task.run();
				}
			}));
		}
	}

	public class WorkTask implements Runnable {

		private boolean isRun;

		private Runnable runnable;

		public WorkTask(boolean isRun, Runnable runnable) {
			this.isRun = isRun;
			this.runnable = runnable;
		}

		@Override
		public void run() {
			runnable.run();
		}

		public boolean isRun() {
			return isRun;
		}

		public void setRun(boolean run) {
			isRun = run;
		}

	}

}
