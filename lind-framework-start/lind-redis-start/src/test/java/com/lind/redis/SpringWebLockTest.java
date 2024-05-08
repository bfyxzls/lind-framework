package com.lind.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2024/1/2 15:41
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
public class SpringWebLockTest {

	@Autowired
	RedissonClient redissonClient;

	@Test
	public void testLock() {
		String lockKey = "myLock";
		// 获取 RLock 对象
		RLock lock = redissonClient.getLock(lockKey);
		try {
			// 尝试获取锁（尝试加锁）（锁超时时间是 30 秒）
			boolean isLocked = lock.tryLock(30, TimeUnit.SECONDS);
			if (isLocked) {
				// 成功获取到锁
				try {
					// 模拟业务处理
					TimeUnit.SECONDS.sleep(50);// 30秒后会自动续期
					System.out.println("成功获取锁，并执行业务代码");
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally {
					// 释放锁
					lock.unlock();
				}
			}
			else {
				// 获取锁失败
				System.err.println("获取锁失败");
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
