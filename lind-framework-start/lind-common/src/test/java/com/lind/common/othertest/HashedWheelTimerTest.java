/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.lind.common.othertest;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
public class HashedWheelTimerTest {

	@Test(timeout = 50000000)
	public void testTimerOverflowWheelLength() throws InterruptedException {
		final HashedWheelTimer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 100,
				TimeUnit.MILLISECONDS, 32);

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(final Timeout timeout) throws Exception {
				log.info("lee1000"); // 打印名字
			}
		}, 1000, TimeUnit.MILLISECONDS);

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(final Timeout timeout) throws Exception {
				log.info("lee10"); // 打印名字
				Thread.sleep(10000);
			}
		}, 1000, TimeUnit.MILLISECONDS);

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(final Timeout timeout) throws Exception {
				log.info("lee2000"); // 打印名字
				Thread.sleep(10000);
			}
		}, 1000, TimeUnit.MILLISECONDS);
		Thread.sleep(10000);
		assertFalse(timer.stop().isEmpty());
	}

	@Test
	public void timer_timeOut_timeTask() throws InterruptedException {
		// 构造一个 Timer 实例
		Timer timer = new HashedWheelTimer();

		// 提交一个任务，让它在 5s 后执行
		Timeout timeout1 = timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) {
				log.info("5s 后执行该任务");
			}
		}, 5, TimeUnit.SECONDS);

		// 再提交一个任务，让它在 10s 后执行
		Timeout timeout2 = timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) {
				log.info("10s 后执行该任务");
			}
		}, 10, TimeUnit.SECONDS);

		// 取消掉那个 5s 后执行的任务
		if (!timeout1.isExpired()) {
			timeout1.cancel();
		}

		// 原来那个 5s 后执行的任务，已经取消了。这里我们反悔了，我们要让这个任务在 3s 后执行，之time2间隔变成了7秒
		// 我们说过 timeout 持有上、下层的实例，所以下面的 timer 也可以写成 timeout1.timer()
		timer.newTimeout(timeout1.task(), 3, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(11);
	}

}
