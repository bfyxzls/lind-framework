package com.lind.redis.limit;

import org.redisson.Redisson;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.Arrays;

public class RedissonSlidingWindowRateLimiter {

	private final String key;

	private final int windowSize;

	private final int maxRequests;

	private final RedissonClient redisson;

	public RedissonSlidingWindowRateLimiter(String key, int windowSize, int maxRequests) {
		this.key = key;
		this.windowSize = windowSize;
		this.maxRequests = maxRequests;
		Config config = new Config();
		JsonJacksonCodec codec = new JsonJacksonCodec();
		config.setCodec(codec);
		config.useSingleServer().setAddress("redis://localhost:6379");
		this.redisson = Redisson.create(config);
	}

	public boolean allowRequest() {
		long currentTime = System.currentTimeMillis();
		long cutoffTime = currentTime - windowSize;

		// 通过Lua脚本执行滑动窗口限流逻辑
		String luaScript = "local time = redis.call('time')\n" +
				"local timestamp = tonumber(time[1]..'.'..string.sub(time[2], 1, 3))\n" +
				"local requests = redis.call('zrangebyscore', KEYS[1], timestamp-ARGV[1], timestamp)\n" +
				"if #requests >= tonumber(ARGV[2]) then\n" +
				"    return 0\n" +
				"end\n" +
				"redis.call('zadd', KEYS[1], timestamp, timestamp)\n" +
				"redis.call('zremrangebyscore', KEYS[1], 0, timestamp-ARGV[1])\n" +
				"return 1";

		RScript script = redisson.getScript();
		Long result = script.eval(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.INTEGER, Arrays.asList(key),
				windowSize, maxRequests);

		return result == 1;
	}

	public void close() {
		redisson.shutdown();
	}

}
