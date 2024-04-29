package com.lind.redis.util;

import com.lind.redis.limit.execption.RedisLimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class RedisRateLimiterPolice {

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
		Optional.ofNullable(runnable).ifPresent(o -> o.run());
	}

	/**
	 * 滑动窗口限流. 需要注意的是，我们要定期清楚过期的key，否则会导致内存泄漏，可以使用ZREMRANGEBYSCORE方法实现.
	 * @param key 限流的key
	 * @param timeWindow 单位时间，秒
	 * @param limit 窗口大小，单位时间最大容许的令牌数
	 * @param runnable 成功后的回调方法
	 */
	public void slidingWindow(String key, int timeWindow, int limit, Runnable runnable) {
		Long currentTime = System.currentTimeMillis();
		if (redisTemplate.hasKey(key)) {
			Long intervalTime = timeWindow * 1000L;
			Long from = currentTime - intervalTime;
			Integer count = redisTemplate.opsForZSet().rangeByScore(key, from, currentTime).size();
			if (count != null && count >= limit) {
				throw new RedisLimitException("每" + timeWindow + "秒最多只能访问" + limit + "次.");
			}
			log.info("from key:{}~{},current count:{}", from, currentTime, count);
		}
		redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(), currentTime);
		Optional.ofNullable(runnable).ifPresent(o -> o.run());
	}

	/**
	 * 清期昨天的zset元素,这块应该写个任务调度，每天执行一次，清量需要的zset元素.
	 * @param key
	 */
	public void delByYesterday(String key) {
		Instant currentInstant = Instant.now();
		Instant oneDayAgoInstant = currentInstant.minusSeconds(86400);
		long oneDayAgoTimeMillis = oneDayAgoInstant.toEpochMilli();
		redisTemplate.opsForZSet().removeRangeByScore(key, 0, oneDayAgoTimeMillis);

	}

}
