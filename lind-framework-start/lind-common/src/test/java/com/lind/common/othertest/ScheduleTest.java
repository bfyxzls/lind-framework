package com.lind.common.othertest;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@EnableScheduling
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleTest {

	@SneakyThrows
	@Test
	public void hutoolSchedule() {
		CronUtil.schedule("0/1 * * * * ?", (Task) () -> {
			System.out.println(new Date() + "开始执行更新程序");
		});
		CronUtil.setMatchSecond(true);
		CronUtil.start();
		Thread.sleep(5000);
	}

}
