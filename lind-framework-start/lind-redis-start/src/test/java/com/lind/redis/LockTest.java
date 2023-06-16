package com.lind.redis;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import com.lind.redis.lock.config.RedisLockConfig;
import com.lind.redis.lock.template.Callback;
import com.lind.redis.lock.template.DistributedLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lind
 * @date 2022/7/13 10:22
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class, RedisLockConfig.class,
		RepeatSubmitController.class, CurrentUser.class })
public class LockTest {

	protected MockMvc mockMvc;

	@Autowired
	DistributedLockTemplate distributedLockTemplate;

	@Autowired
	private WebApplicationContext webApplicationContext;

	void lock5Second() {
		// 锁最大排除时等待的时间，超过这个时间还没拿到锁，那就放弃.

		distributedLockTemplate.execute("订单流水号", 70, TimeUnit.SECONDS, new Callback() {
			@Override
			public Object onGetLock() throws InterruptedException {
				// 获得锁后要做的事
				log.info("{} 拿到锁，需要5秒钟，这时有请求打入应该被阻塞或者拒绝", Thread.currentThread().getName());
				TimeUnit.SECONDS.sleep(80);
				log.info("大任务执行完成");
				return null;
			}

			@Override
			public Object onTimeout() throws InterruptedException {
				// 获取锁超时后要做的事
				log.info("{} 没拿到锁", Thread.currentThread().getName());
				return null;
			}
		});
	}

	@Test
	public void lock() throws InterruptedException {
		Thread thread1 = new Thread(() -> lock5Second());
		Thread thread2 = new Thread(() -> lock5Second());
		Thread thread3 = new Thread(() -> lock5Second());

		thread1.start();
		thread2.start();
		thread3.start();
		TimeUnit.SECONDS.sleep(120L);
	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void tokenIndex() throws Exception {
		for (int i = 1; i < 3; i++)
			mockMvc.perform(get("/get")).andDo(print()).andExpect(status().isOk());

	}

}
