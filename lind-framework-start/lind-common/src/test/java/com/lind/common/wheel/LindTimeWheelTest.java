package com.lind.common.wheel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2023/6/8 17:27
 * @since 1.0.0
 */
@Slf4j
public class LindTimeWheelTest {

	// 示例使用
	public static void main(String[] args) {
		log.info("任务开始");
		LindTimeWheel timeWheel = new LindTimeWheel(1000, 10); // 每秒一个槽，总共10个槽
		timeWheel.addTask(() -> log.info("Task 1 executed"), 3000); // 3秒后执行任务1
		timeWheel.addTask(() -> log.info("Task 2 executed"), 5000); // 5秒后执行任务2
		timeWheel.addTask(() -> log.info("Task 3 executed"), 7000); // 7秒后执行任务3
		try {
			TimeUnit.SECONDS.sleep(10);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeWheel.stop();
		// 显式关闭线程池并等待任务执行完成
		timeWheel.awaitTermination();
	}

	@Test
	public void incMod() {
		for (int i = 1000; i < 1010; i++) {
			System.out.println("i mod 10=" + i % 10);
		}
	}

	@Test
	public void cpu() {
		log.info(String.valueOf(Runtime.getRuntime().availableProcessors()));
	}

}
