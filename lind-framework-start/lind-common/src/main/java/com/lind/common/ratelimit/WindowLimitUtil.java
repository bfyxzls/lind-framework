package com.lind.common.ratelimit;

/**
 * 固定窗口限流
 *
 * @author lind
 * @date 2023/12/19 15:08
 * @since 1.0.0
 */
public class WindowLimitUtil {

	public static final Long windowUnit = 1000L; // 假设固定时间窗口是1000ms

	public static final Integer threshold = 10; // 窗口阀值是10

	public static Integer counter = 0; // 统计请求数

	public static long lastAcquireTime = 0L;

	/**
	 * 固定窗口时间算法
	 * @return
	 */
	public synchronized boolean fixedWindowsTryAcquire() {
		long currentTime = System.currentTimeMillis(); // 获取系统当前时间
		if (currentTime - lastAcquireTime > windowUnit) { // 检查是否在时间窗口内
			counter = 0; // 计数器清0
			lastAcquireTime = currentTime; // 开启新的时间窗口
		}
		if (counter < threshold) { // 小于阀值
			counter++; // 计数统计器加1
			return true;
		}
		return false;
	}

}
