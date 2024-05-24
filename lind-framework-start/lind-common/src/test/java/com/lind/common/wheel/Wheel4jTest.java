package com.lind.common.wheel;

import com.lind.common.wheel.wheel4j.Timer;
import com.lind.common.wheel.wheel4j.TimerLauncher;
import com.lind.common.wheel.wheel4j.TimerTask;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2024/5/24 11:48
 * @since 1.0.0
 */
public class Wheel4jTest {

	@Test
	public void taskList() throws InterruptedException {
		Timer timer = new TimerLauncher();
		timer.add(new TimerTask("task1", 6000));
		timer.add(new TimerTask("task2", 10000));
		timer.add(new TimerTask("task3", 15000));
		Thread.sleep(60000);

	}

}
