package com.lind.common.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 滑动窗口算法.
 *
 * @author lind
 * @date 2023/12/19 15:09
 * @since 1.0.0
 */
public class SlidingWindowLimitUtil {

	private static final Logger logger = LoggerFactory.getLogger(SlidingWindowLimitUtil.class);

	/**
	 * 计数器, k-为当前窗口的开始时间值秒，value为当前窗口的计数
	 */
	private final TreeMap<Long, Integer> counters = new TreeMap<>();

	/**
	 * 单位时间划分的小周期（单位时间是1分钟，10s一个小格子窗口，一共6个格子，计的数为6个格子的和）
	 */
	private int SUB_CYCLE = 10;

	/**
	 * 每1分钟限流请求数
	 */
	private int thresholdPerMin = 100;

	/**
	 * 滑动窗口时间算法实现
	 */
	public synchronized boolean slidingWindowsTryAcquire() {
		/**
		 * 获取当前时间，并将其转换为UTC时区下的秒级时间戳，并对齐到小周期窗口 这么做的原因可能是为了将时间戳对齐到最近的10秒间隔。通过除以10再乘以10的操作，
		 * 可以将当前时间戳向下取整到最近的10秒整数倍。这样做的目的可能是为了实现某种基于时间窗口的逻辑， 例如按时间窗口统计数据、限流等场景。
		 *
		 * 总的来说，这行代码的作用是将当前时间戳向下取整到最近的10秒整数倍，以便用于时间窗口相关的逻辑处理。
		 */
		long currentWindowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / SUB_CYCLE * SUB_CYCLE; // 获取当前时间在哪个小周期窗口
		int currentWindowNum = countCurrentWindow(currentWindowTime); // 当前窗口总请求数

		// 超过阀值限流
		if (currentWindowNum >= thresholdPerMin) {
			return false;
		}

		// 计数器+1
		if (counters.containsKey(currentWindowTime)) {
			counters.put(currentWindowTime, counters.get(currentWindowTime) + 1);
		}
		else {
			counters.put(currentWindowTime, 1);
		}
		return true;
	}

	/**
	 * 统计当前窗口的请求数
	 */
	private int countCurrentWindow(long currentWindowTime) {
		// 计算窗口开始位置，一点一点的向前走，把1分钟60/10分成6份，取最近5份的数据，丢掉离startTime最早的一条数据
		// 周而复始，就成了滑动的窗口了
		long startTime = currentWindowTime - SUB_CYCLE * (60 / SUB_CYCLE - 1);
		logger.info("currentWindowTime={},startTime={}", currentWindowTime, startTime);
		int count = 0;

		// 遍历存储的计数器
		Iterator<Map.Entry<Long, Integer>> iterator = counters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, Integer> entry = iterator.next();
			// 删除无效过期的子窗口计数器
			if (entry.getKey() < startTime) {
				iterator.remove();
			}
			else {
				// 累加当前窗口的所有计数器之和
				count = count + entry.getValue();
			}
		}
		return count;
	}

}
