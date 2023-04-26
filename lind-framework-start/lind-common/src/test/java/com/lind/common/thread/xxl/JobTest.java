package com.lind.common.thread.xxl;

import org.junit.Test;

/**
 * @author lind
 * @date 2022/11/10 17:30
 * @since 1.0.0
 */
public class JobTest {

	@Test
	public void run() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			Execute.registJobThread(i);
			Thread.sleep(1000);
		}
	}

}
