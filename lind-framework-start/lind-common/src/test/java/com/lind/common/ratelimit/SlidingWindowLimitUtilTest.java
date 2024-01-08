package com.lind.common.ratelimit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2023/12/19 15:17
 * @since 1.0.0
 */
@Slf4j
public class SlidingWindowLimitUtilTest {

	@Test
	public void run10Qps() throws InterruptedException {
		SlidingWindowLimitUtil slidingWindowLimitUtil = new SlidingWindowLimitUtil();
		for (int i = 0; i < 100; i++) {
			boolean result = slidingWindowLimitUtil.slidingWindowsTryAcquire();
			System.out.println(i + " " + result);
			Thread.sleep(1000);
		}
	}

	/**
	 * 循序渐进. learn to walk before you run.
	 */
	@SneakyThrows
	@Test
	public void stepByStepMinute() {
		int windowSize = 10;
		int scale = 60;
		for (int i = 0; i < 30; i++) {
			long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
			currentSecond = currentSecond / windowSize * windowSize; // 将当前时间戳向下取整到最近的10秒整数倍，即10秒内的时间戳是一个值

			long startTime = currentSecond - (windowSize * (scale / windowSize - 1));
			log.info("currentSecond={},startTime={}", currentSecond, startTime);

			Thread.sleep(1000);
		}

	}

	/**
	 * 将1秒分钟10个小周期
	 */
	@Test
	public void stepByStepSecond() throws InterruptedException {
		int windowSize = 10;
		int scale = 1000;
		for (int i = 0; i < 100; i++) {
			long timestamp = System.currentTimeMillis();
			// 把1秒分成10个小窗口，一共可以分成100个小窗口，每个小窗口里的10个元素他们的currentMillisSecond是相同的
			long currentMillisSecond = timestamp / windowSize * windowSize;
			long startTime = currentMillisSecond - (windowSize * (scale / windowSize - 1));
			// 1毫秒比较短，它大于上面代码执行时间，所以时间窗口打印的日志并不是10条
			log.info("timestamp={},currentMillisSecond={},startTime={}", timestamp, currentMillisSecond, startTime);
			TimeUnit.MILLISECONDS.sleep(1);
		}
	}

}
