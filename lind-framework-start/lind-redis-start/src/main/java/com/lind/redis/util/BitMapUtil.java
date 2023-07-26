package com.lind.redis.util;

import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis位图工具类
 */
@Component
public class BitMapUtil {

	private static RedissonClient redissonClient;

	public static RBitSet getBitSet(String key) {
		return redissonClient.getBitSet(key);
	}

	public static void setBitSet(String key, long index) {
		RBitSet bitSet = redissonClient.getBitSet(key);
		bitSet.set(index);
		bitSet.expire(7, TimeUnit.DAYS);
	}

	@Autowired
	public void setRedissonClient(RedissonClient redissonClient) {
		BitMapUtil.redissonClient = redissonClient;
	}

}
