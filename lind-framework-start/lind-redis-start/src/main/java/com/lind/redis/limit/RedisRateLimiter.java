package com.lind.redis.limit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class RedisRateLimiter {

	/**
	 * redis客户端
	 */
	private final RedisTemplate redisTemplate;

	/**
	 * redisson客户端
	 */
	private final RedissonClient redisson;

	/**
	 * 令牌桶限流
	 * @param key 限流的key
	 * @param unitSecond 单位时间，秒
	 * @param count 单位时间产生令牌数
	 * @param runnable 成功后的回调方法
	 */
	public void tokenBucket(String key, int unitSecond, int count, Runnable runnable) {
		RRateLimiter rateLimiter = redisson.getRateLimiter(key);
		// 最大流速 = 每unitSecond秒钟产生count个令牌
		rateLimiter.trySetRate(RateType.OVERALL, count, unitSecond, RateIntervalUnit.SECONDS);
		// 需要1个令牌
		if (!rateLimiter.tryAcquire(1)) {
			throw new IllegalArgumentException("令牌桶无可用令牌，每" + unitSecond + "秒产生" + count + "个令牌");
		}
		log.info("访问成功");
		Optional.ofNullable(runnable).ifPresent(o -> o.run());

	}

	/**
	 * 滑动窗口限流.
	 * @param key 限流的key
	 * @param unitSecond 单位时间，秒
	 * @param windowSize 窗口大小，单位时间可生产的令牌数
	 * @param runnable 成功后的回调方法
	 */
	public void slidingWindow(String key, int unitSecond, int windowSize, Runnable runnable) {
		Long currentTime = System.currentTimeMillis();
		System.out.println(currentTime);
		if (redisTemplate.hasKey(key)) {
			// intervalTime是限流的时间
			Long intervalTime = unitSecond * 1000L;
			Integer count = redisTemplate.opsForZSet().rangeByScore(key, currentTime - intervalTime, currentTime)
					.size();
			System.out.println(count);
			if (count != null && count > windowSize) {
				throw new IllegalArgumentException("每" + unitSecond + "秒最多只能访问" + windowSize + "次.");
			}
		}
		redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(), currentTime);
		log.info("访问成功");
		Optional.ofNullable(runnable).ifPresent(o -> o.run());
	}

}
