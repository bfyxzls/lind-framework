package com.lind.redis;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import com.lind.redis.limit.execption.RedisLimitException;
import com.lind.redis.util.RedisRateLimiterPolice;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class })
/**
 * @author lind
 * @date 2022/7/13 10:22
 * @since 1.0.0
 */
public class LimitTest {

	static String WINDOW_KEY = "request_bucket2";

	@Autowired
	RedisTemplate redisTemplate;

	RedissonClient redisson;

	/**
	 * 最小窗口
	 * @param s
	 * @param t
	 * @return
	 */
	private String minWindow(String s, String t) {
		Map<Character, Integer> need = new HashMap<>();
		Map<Character, Integer> window = new HashMap<>();
		// 将t对应字符及次数存储到map
		for (char c : t.toCharArray()) {
			need.put(c, need.getOrDefault(c, 0) + 1);
		}
		int left = 0, right = 0;
		// valid 变量表示窗⼝中满⾜ need 条件的字符个数；
		// 如果 valid 和 need.size 的⼤⼩相同，则说明窗⼝已满⾜条件，已经完全覆盖了串 T
		int valid = 0;
		// 记录最⼩覆盖⼦串的起始索引及⻓度
		int start = 0, len = Integer.MAX_VALUE;
		while (right < s.length()) {
			// c 是将移⼊窗⼝的字符
			char c = s.charAt(right);
			// 右移窗⼝
			right++;
			if (need.containsKey(c)) {
				window.put(c, window.getOrDefault(c, 0) + 1);
				if (need.get(c).equals(window.get(c))) {
					valid++;
				}
			}
			// 判断左侧窗⼝是否要收缩
			while (valid == need.size()) {
				// 在这⾥更新最⼩覆盖⼦串
				if (right - left < len) {
					len = right - left;
					start = left;
				}
				// d 是将移出窗⼝的字符
				char d = s.charAt(left);
				// 左移窗⼝
				left++;
				if (need.containsKey(d)) {
					if (need.get(d).equals(window.get(d))) {
						valid--;
					}
					window.put(d, window.get(d) - 1);
				}
			}
		}
		// 返回最⼩覆盖⼦串
		return len == Integer.MAX_VALUE ? " " : s.substring(start, start + len);
	}

	@Test
	public void minWindow() {
		String source = "ADOBECODEBANC";
		String target = "ABC";
		LimitTest solution = new LimitTest();
		System.out.println(solution.minWindow(source, target));
	}

	/**
	 * zset实现，[当前时间,(当前时间-限流时间)].length>阈值，就需要限流了
	 */
	@Test
	public void zSetSlidingWindowTest() throws InterruptedException {
		RedisRateLimiterPolice redisRateLimiter = new RedisRateLimiterPolice(redisTemplate, redisson);
		for (int i = 0; i < 200; i++) {
			try {
				redisRateLimiter.slidingWindow(WINDOW_KEY, 10, 5, null);
				Thread.sleep(1000);
			}
			catch (RedisLimitException ex) {
				log.error(ex.toString());
				Thread.sleep(2000);
			}

		}
	}

	/**
	 * Zset实现的滑动窗口
	 * @return
	 */

	@Test
	public void tokenBucketTest() throws InterruptedException {
		RedisRateLimiterPolice redisRateLimiter = new RedisRateLimiterPolice(redisTemplate, redisson);

		for (int i = 0; i < 20; i++) {
			redisRateLimiter.tokenBucket("bucket", 10, 5, null);
			Thread.sleep(1000);
		}
	}

	@Before
	public void init() {
		// 初始化Redisson客户端
		Config config = new Config();
		JsonJacksonCodec codec = new JsonJacksonCodec();
		config.setCodec(codec);
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		redisson = Redisson.create(config);
	}

	@After
	public void close() {
		redisson.shutdown();
	}

	/**
	 * 令牌桶限流
	 */
	@Test
	public void countDownLatch() throws InterruptedException {
		final String BUCKET_KEY = "request_bucket";
		final long BUCKET_CAPACITY = 100; // 桶的容量
		final long RATE_LIMIT_PER_SECOND = 5; // 每秒生成的令
		RCountDownLatch bucket = redisson.getCountDownLatch(BUCKET_KEY);
		bucket.trySetCount(BUCKET_CAPACITY);

		// 模拟请求处理
		for (int i = 1; i <= 50; i++) {
			if (bucket.getCount() > 0) {
				log.info("处理请求 " + i);
				bucket.countDown(); // 消耗一个令牌
			}
			else {
				log.error("限流，请求 " + i + " 被拒绝");
			}
			Thread.sleep(1000 / RATE_LIMIT_PER_SECOND); // 休眠一定时间，模拟请求间隔
		}
	}

}
