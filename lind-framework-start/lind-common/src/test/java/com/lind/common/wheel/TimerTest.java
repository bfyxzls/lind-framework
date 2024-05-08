package com.lind.common.wheel;

import com.lind.common.wheel.hashwheel.HashedWheelTimer;
import com.lind.common.wheel.hashwheel.WaitStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TimerTest {

	HashedWheelTimer timer;

	@BeforeEach
	public void before() {
		timer = new HashedWheelTimer(TimeUnit.MILLISECONDS.toMillis(1000), 8, new WaitStrategy.SleepWait());
	}

	@AfterEach
	public void after() throws InterruptedException {
		timer.shutdownNow();
		Assert.assertTrue(timer.awaitTermination(10, TimeUnit.SECONDS));
	}

	@Test
	public void testLindHashWheelTimer() throws InterruptedException {
		log.info("start");
		timer.schedule(() -> {
			log.info("hello3");
		}, 3, TimeUnit.SECONDS);
		timer.schedule(() -> {
			log.info("hello5");
		}, 5, TimeUnit.SECONDS);
		timer.schedule(() -> {
			log.info("hello8");
		}, 10, TimeUnit.SECONDS);
		Thread.sleep(10000);
	}

	@Test
	public void oneShotRunnableTest() throws InterruptedException {
		AtomicInteger i = new AtomicInteger(1);
		timer.schedule(() -> {
			i.decrementAndGet();
		}, 100, TimeUnit.MILLISECONDS);
		Thread.sleep(300);
		Assert.assertEquals(i.get(), 0);
	}

	@Test
	public void oneShotRunnableFutureTest() throws InterruptedException, ExecutionException, TimeoutException {
		AtomicInteger i = new AtomicInteger(1);
		long start = System.currentTimeMillis();
		MatcherAssert.assertThat(
				timer.schedule(i::decrementAndGet, 100, TimeUnit.MILLISECONDS).get(10, TimeUnit.SECONDS), Is.is(0));
		long end = System.currentTimeMillis();
		Assert.assertEquals(i.get(), 0);
		Assert.assertTrue(end - start >= 100);
	}

	@Test
	public void oneShotCallableTest() throws InterruptedException {
		AtomicInteger i = new AtomicInteger(1);
		timer.schedule(() -> {
			i.decrementAndGet();
			return "Hello";
		}, 100, TimeUnit.MILLISECONDS);
		Thread.sleep(300);
		Assert.assertEquals(i.get(), 0);
	}

	@Test
	public void oneShotCallableFuture() throws InterruptedException, TimeoutException, ExecutionException {
		AtomicInteger i = new AtomicInteger(1);
		long start = System.currentTimeMillis();
		Future<String> future = timer.schedule(() -> {
			i.decrementAndGet();
			return "Hello";
		}, 900, TimeUnit.MILLISECONDS);
		MatcherAssert.assertThat(future.get(1, TimeUnit.SECONDS), Is.is("Hello"));
		long end = System.currentTimeMillis();
		Assert.assertEquals(i.get(), 0);
		Assert.assertTrue(end - start >= 100);
	}

	@Test
	public void executionOnTime() throws InterruptedException {
		int scheduledTasks = 100000;
		int tickDuration = 10;
		int timeout = 130;

		int minTimeout = timeout - tickDuration,
				maxTimeout = timeout + (tickDuration * 8 /* Wheel size */);
		long[] delays = new long[scheduledTasks];

		CountDownLatch latch = new CountDownLatch(scheduledTasks);
		for (int i = 0; i < scheduledTasks; i++) {
			long start = System.nanoTime();
			int idx = i;
			timer.schedule(() -> {
				delays[idx] = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
				latch.countDown();
			}, timeout, TimeUnit.MILLISECONDS);
		}

		latch.await();

		for (int i = 0; i < scheduledTasks; i++) {
			long delay = delays[i];
			Assert.assertTrue(delay >= minTimeout && delay < maxTimeout,
					String.format("Timeout %s delay must be %s < %s < %s", i, minTimeout, delay, maxTimeout));
		}
	}

	@Test
	public void fixedRateFirstFireTest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		timer.scheduleAtFixedRate(latch::countDown, 100, 100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
		long end = System.currentTimeMillis();
		Assert.assertTrue(end - start >= 100);
	}

	@Test
	public void fixedRateSubsequentFireTest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(10);
		long start = System.currentTimeMillis();
		timer.scheduleAtFixedRate(latch::countDown, 100, 100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(latch.await(2, TimeUnit.SECONDS));
		long end = System.currentTimeMillis();
		Assert.assertTrue(end - start >= 1000);
	}

	@Test
	public void delayBetweenFixedRateEvents() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(2);
		List<Long> r = new ArrayList<>();
		timer.scheduleAtFixedRate(() -> {
			r.add(System.currentTimeMillis());
			latch.countDown();

			if (latch.getCount() == 0) {
				return;
			}

			try {
				Thread.sleep(90);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			r.add(System.currentTimeMillis());

		}, 100, 100, TimeUnit.MILLISECONDS);

		// time difference between the beginning of second tick and end of first one
		Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
		Assert.assertTrue(r.get(2) - r.get(1) <= 100);
	}

	@Test
	public void fixedDelayFirstFireTest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		timer.scheduleWithFixedDelay(latch::countDown, 100, 100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
		long end = System.currentTimeMillis();
		Assert.assertTrue(end - start >= 100);
	}

	@Test
	public void fixedDelaySubsequentFireTest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(10);
		long start = System.currentTimeMillis();
		timer.scheduleWithFixedDelay(latch::countDown, 100, 100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
		long end = System.currentTimeMillis();
		Assert.assertTrue(end - start >= 1000);
	}

	@Test
	public void delayBetweenFixedDelayEvents() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(2);
		List<Long> r = new ArrayList<>();
		timer.scheduleWithFixedDelay(() -> {
			r.add(System.currentTimeMillis());

			latch.countDown();

			if (latch.getCount() == 0) {
				return;
			}

			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			r.add(System.currentTimeMillis());
		}, 100, 100, TimeUnit.MILLISECONDS);
		Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
		// time difference between the beginning of second tick and the first one
		Assert.assertTrue(r.get(2) - r.get(1) >= 10);
	}

	@Test
	public void nettyTest() throws InterruptedException {

		io.netty.util.HashedWheelTimer wheelTimer = new io.netty.util.HashedWheelTimer();
		wheelTimer.newTimeout(timeout -> log.info("1s delay"), 1, TimeUnit.SECONDS);
		wheelTimer.newTimeout(timeout -> log.info("10s delay"), 10, TimeUnit.SECONDS);
		wheelTimer.newTimeout(timeout -> log.info("11s delay"), 11, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(20);

	}

	@SneakyThrows
	@Test
	public void jdkTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println(new Date() + "123");
			}
		}, 4000, 2000); // 4000->4秒后执行 2000->间隔2秒

		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println(new Date() + "456");
			}
		}, 2000);// 2000->2秒后执行后就停了
		Thread.sleep(10000);
	}

}
