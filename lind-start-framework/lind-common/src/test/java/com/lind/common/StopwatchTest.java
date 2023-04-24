package com.lind.common;

import com.lind.common.util.StopWatchUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.util.StopWatch;

public class StopwatchTest {

	@SneakyThrows
	@Test
	public void testTime() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("testTime");
		Thread.sleep(100);
		stopWatch.stop();
		System.out.println(stopWatch.getLastTaskTimeMillis());
		stopWatch.start("testTime2000");
		Thread.sleep(200);
		stopWatch.stop();
		System.out.println(stopWatch.getLastTaskTimeMillis());
	}

	@SneakyThrows
	@Test
	public void prettyPrint() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("testTime");
		Thread.sleep(100);
		stopWatch.stop();
		stopWatch.start("testTime2000");
		Thread.sleep(200);
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}

	@Test
	public void runTime() {
		StopWatchUtils.runTimer("睡眠", () -> {
			try {
				Thread.sleep(150);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		StopWatchUtils.runTimer("唤醒", () -> {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

}
