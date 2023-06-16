package com.lind.redis.lock.template;

import com.lind.redis.lock.config.RedisLockProperty;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@RequiredArgsConstructor
public class RedisLockTemplate implements DistributedLockTemplate {

	/**
	 * 注入这个对象需要注册，锁的默认有期时间是60秒，在这个时间内程序没有执行完，锁会自动释放，如果希望有看门狗需要考虑用redisson组件.
	 */
	private final RedisLockRegistry redisLockRegistry;

	private final RedisLockProperty redisLockProperty;

	@SneakyThrows // 向上层抛出异常
	@Override
	public Object execute(String lockId, Integer timeout, TimeUnit unit, Callback callback) {
		Lock lock = null;
		boolean getLock = false;
		try {
			lock = redisLockRegistry.obtain(lockId);

			if (redisLockProperty.getInterrupt()) {
				getLock = lock.tryLock();// 中断执行,立即返回
			}
			else {
				getLock = lock.tryLock(timeout, unit); // 阻塞执行,实现可重入锁，每100ms重试一次
			}
			if (getLock) {
				// 拿到锁
				return callback.onGetLock();
			}
			else {
				// 未拿到锁，它会进行阻塞
				return callback.onTimeout();
			}
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		finally {
			if (getLock) {
				// 释放锁
				lock.unlock();
			}
		}
	}

}
