package com.lind.common.timewheel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 这个延时队列有些问题.
 *
 * @author lind
 * @date 2024/5/27 16:21
 * @since 1.0.0
 */
@Slf4j
public class SimpleTimeWheel2 {

	private static final int TIME_UNIT = 1000; // 时间单位，假设为100ms

	private static final int WHEEL_SIZE = 8; // 时间轮大小

	private static final int TICK_INTERVAL = 1; // 每次滴答的间隔，假设为1个时间单位

	private ScheduledExecutorService executor;

	private ScheduledFuture<?> future;

	private int currentTime = 0;

	private DelayQueue<Task>[] buckets;

	public SimpleTimeWheel2() {
		this.executor = Executors.newScheduledThreadPool(1);
		this.buckets = new DelayQueue[WHEEL_SIZE];
		for (int i = 0; i < WHEEL_SIZE; i++) {
			this.buckets[i] = new DelayQueue<>();
		}
	}

	public static void main(String[] args) {
		SimpleTimeWheel2 timeWheel = new SimpleTimeWheel2();
		timeWheel.start();

		// 添加一个延时任务
		timeWheel.addTask(5000, () -> log.info("延时5000ms执行的任务"));
		timeWheel.addTask(15000, () -> log.info("延时15000ms执行的任务"));
		timeWheel.addTask(25000, () -> log.info("延时25000ms执行的任务"));

		// 等待一段时间，保证任务执行完毕
		try {
			Thread.sleep(60000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		timeWheel.stop();
	}

	@SneakyThrows
	@Test
	public void testDelay() {
		DelayQueue<Task> task = new DelayQueue<>();
		task.add(new Task(1000, () -> {
			log.info("1 s...");
		}));
		task.add(new Task(5000, () -> {
			log.info("5 s...");
		}));
		task.add(new Task(10000, () -> {
			log.info("10 s...");
		}));

		while (true) {
			Task a = task.poll(8, TimeUnit.SECONDS);
			if (a == null) {
				break;
			}
			a.task.run();
		}
	}

	public void start() {
		this.future = this.executor.scheduleAtFixedRate(this::tick, 5, TICK_INTERVAL * TIME_UNIT,
				TimeUnit.MILLISECONDS);
	}

	public void stop() {
		this.future.cancel(true);
		this.executor.shutdown();
	}

	public void addTask(int delay, Runnable task) {
		int targetTime = (currentTime + delay / TIME_UNIT) % WHEEL_SIZE;
		buckets[targetTime].offer(new Task(delay, task));
	}

	@SneakyThrows
	private void tick() {
		int currentBucketIndex = currentTime % WHEEL_SIZE;
		DelayQueue<Task> currentBucket = buckets[currentBucketIndex];
		while (!currentBucket.isEmpty()) {
			Task task = currentBucket.poll(1, TimeUnit.SECONDS);
			if (task != null) {
				executor.execute(task.task);
			}

		}
		currentTime = (currentTime + 1) % WHEEL_SIZE;
	}

	class Task implements Delayed {

		private final long expireTime;

		Runnable task;

		public Task(long expireTime, Runnable task) {
			this.task = task;
			this.expireTime = System.currentTimeMillis() + expireTime;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long diff = expireTime - System.currentTimeMillis();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo(Delayed o) {
			long diff = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
			return Long.compare(diff, 0);
		}

	}

}
