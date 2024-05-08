package com.lind.redis;

import com.lind.redis.lock.template.Callback;
import com.lind.redis.lock.template.RedisLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2024/1/2 16:00
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
public class RedisLockTemplateTest {

	@Autowired
	RedisLockTemplate redisLockTemplate;

	@Test
	public void testLock() {
		redisLockTemplate.execute("test_lock", 10, TimeUnit.SECONDS, new Callback() {
			@Override
			public Object onGetLock() throws InterruptedException {
				System.out.println("成功获取锁，并执行业务代码");
				TimeUnit.SECONDS.sleep(70);// 60秒后锁释放，无看门狗
				return null;
			}

			@Override
			public Object onTimeout() throws InterruptedException {
				System.out.println("未获取到锁");
				return null;
			}
		});
	}

}
