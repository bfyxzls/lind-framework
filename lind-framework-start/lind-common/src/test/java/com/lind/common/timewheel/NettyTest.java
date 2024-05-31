package com.lind.common.timewheel;

import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2023/5/29 13:39
 * @since 1.0.0
 */
@Slf4j
public class NettyTest {

	// CountDownLatch的作用很简单，就是一个或者一组线程在开始执行操作之前，必须要等到其他线程执行完才可以
	private final CountDownLatch countDownLatch = new CountDownLatch(2);

	@Test
	public void test1() throws Exception {
		// 定义一个HashedWheelTimer，有16个格的轮子，每一秒走一个一个格子
		HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 16);
		// 把任务加到HashedWheelTimer里，到了延迟的时间就会自动执行
		timer.newTimeout((timeout) -> {
			log.info("task1 execute");
			countDownLatch.countDown();
		}, 5, TimeUnit.SECONDS);
		timer.newTimeout((timeout) -> {
			log.info("task2 execute");
			countDownLatch.countDown();
		}, 10, TimeUnit.SECONDS);
		countDownLatch.await();
		timer.stop();
		Thread.sleep(10000);
	}

}
