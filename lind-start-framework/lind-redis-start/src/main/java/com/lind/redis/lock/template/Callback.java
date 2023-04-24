package com.lind.redis.lock.template;

/**
 * 回调方法 https://github.com/yujiasun/Distributed-Kit
 */
public interface Callback {

	/**
	 * 成功获取锁后执行方法.
	 */
	Object onGetLock() throws InterruptedException;

	/**
	 * 获取锁超时回调.
	 */
	Object onTimeout() throws InterruptedException;

}
