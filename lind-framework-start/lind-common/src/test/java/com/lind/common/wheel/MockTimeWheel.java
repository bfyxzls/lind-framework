package com.lind.common.wheel;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lind
 * @date 2023/8/23 14:12
 * @since 1.0.0
 */
@Slf4j
public class MockTimeWheel {

	public static void main(String[] args) {
		MockTimeWheel.TimeWheel timeWheel = new TimeWheel(60, 1000); // 16曹，每槽1000ms，共可处理的时间延时是16秒

		timeWheel.addTask(() -> log.info("Task 1 executed"), 3000);
		timeWheel.addTask(() -> log.info("Task 2 executed"), 5000);
		timeWheel.addTask(() -> log.info("Task 3 executed"), 30000);

		while (true) {
			try {
				Thread.sleep(1000); // 模拟时间推进
				timeWheel.advance();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 时间轮槽中的任务
	static class Task {

		Runnable task;

		public Task(Runnable task) {
			this.task = task;
		}

	}

	// 时间轮槽
	static class Slot {

		List<Task> tasks = new ArrayList<>();

	}

	// 时间轮
	static class TimeWheel {

		private final int size; // 时间轮槽数

		private int currentIndex; // 当前槽索引

		private final Slot[] slots; // 时间轮槽数组

		private final long tickMs; // 每个槽的时间间隔

		public TimeWheel(int size, long tickMs) {
			this.size = size;
			this.tickMs = tickMs;
			this.slots = new Slot[size];
			for (int i = 0; i < size; i++) {
				slots[i] = new Slot();
			}
			this.currentIndex = 0;
		}

		// 添加任务到时间轮
		public void addTask(Runnable task, long delayMs) {
			int ticks = (int) (delayMs / tickMs);// 延时需要转动的格数
			int slotIndex = (currentIndex + ticks) % size;// 当前索引位置+转动的格数=新任务所在的槽索引
			slots[slotIndex].tasks.add(new Task(task));
		}

		// 推进时间轮，执行当前槽中的任务
		public void advance() {
			Slot currentSlot = slots[currentIndex];
			for (Task task : currentSlot.tasks) {
				task.task.run();
			}
			currentSlot.tasks.clear();
			currentIndex = (currentIndex + 1) % size;
		}

	}

}
