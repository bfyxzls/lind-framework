package com.lind.redis.lock.template;

import com.lind.redis.config.RedisLockProperty;
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

	/**
	 * 分布锁，最大60秒锁定，没有看门狗机制.
	 * @param lockId 锁id(对应业务唯一ID)
	 * @param timeout 最大等待获取锁时间，当有线程占用时，其它线程等待（阻塞）时间，当你希望任务排队执行时，这个值可以设大一些，这样保证每个线程都可以执行任务
	 * @param unit 等待时间单位，当出现锁竞争时，它多长时间去重新获取锁
	 * @param callback 回调方法
	 * @return
	 */
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
				getLock = lock.tryLock(timeout, unit); // 阻塞执行,实现可重入锁，每timeout重试一次
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
