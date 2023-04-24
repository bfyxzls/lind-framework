package com.lind.redis.lock.template;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁模板类.
 */
public interface DistributedLockTemplate {

	/**
	 * 执行方法.
	 * @param lockId 锁id(对应业务唯一ID)
	 * @param timeout 最大等待获取锁时间，当有线程占用时，其它线程等待（阻塞）时间，当你希望任务排队执行时，这个值可以设大一些，这样保证每个线程都可以执行任务
	 * @param unit 等待时间单位
	 * @param callback 回调方法
	 */
	Object execute(String lockId, Integer timeout, TimeUnit unit, Callback callback);

}
