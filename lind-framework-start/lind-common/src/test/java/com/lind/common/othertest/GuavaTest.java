package com.lind.common.othertest;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/7/3 16:34
 * @since 1.0.0
 */
@Slf4j
public class GuavaTest {

	// Guava 的 RateLimiter 实现速率限制
	@Test
	public void rateLimiter() {
		// Create a RateLimiter with a rate of 2 permits per second
		RateLimiter rateLimiter = RateLimiter.create(2.0);
		for (int i = 1; i <= 10; i++) {
			// Acquire a permit from the RateLimiter
			// 在每个操作之前，添加下面这行代码
			double waitTime = rateLimiter.acquire();
			// Simulate some operation
			System.out.println("Waited for " + waitTime + " seconds");
			// Sleep for 1 second to simulate the duration of the operation
			/**
			 * Waited for 0.0 seconds Waited for 0.499143 seconds Waited for 0.49862
			 * seconds Waited for 0.499441 seconds Waited for 0.500231 seconds Waited for
			 * 0.495163 seconds Waited for 0.499202 seconds Waited for 0.499488 seconds
			 * Waited for 0.500022 seconds Waited for 0.497679 seconds
			 */
		}
	}

}
