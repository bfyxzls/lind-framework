package com.lind.redis;

import com.lind.redis.config.LettuceRedisAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 关于ip地址范围检索的测试
 *
 * @author lind
 * @date 2024/3/6 11:14
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class })
public class IpRangeTest {

	private static final String IP_RANGE_KEY = "ip_ranges";

	private static final String IP_SCORE_RANGE_KEY = "ip_score_ranges";

	private static final String ZSET_TEST = "zset_test";

	private static final String SET_TEST = "set_test";

	@Autowired
	RedisTemplate redisTemplate;

	// 将 IP 地址转换为 long 类型的分数值
	public static long convertIPToScore(String ip) {
		String[] ipParts = ip.split("\\.");
		if (ipParts.length != 4) {
			throw new IllegalArgumentException("Invalid IP address format");
		}

		long score = 0;
		for (int i = 0; i < 4; i++) {
			long partValue = Long.parseLong(ipParts[i]);
			score = (score << 8) + partValue; // 将每个部分的值左移8位并相加
		}
		return score;
	}

	@Test
	public void ipToLong() {
		convertIPToScore("192.168.1.1"); // 3232235777
		double result = 192 * Math.pow(256, 3) + 168 * Math.pow(256, 2) + 1 * 256 + 1;
		System.out.println("result=" + result);

	}

	@Test
	public void zsetFindIp() {
		redisTemplate.opsForSet().add(SET_TEST, "115.207.252.109-115.207.252.109");
		redisTemplate.opsForSet().members(SET_TEST).forEach(o -> {
			String[] arr = StringUtils.split(o.toString(), "-");
			long start = convertIPToScore(arr[0]);
			System.out.println("start=" + start);

		});
	}

	@Test
	public void testZSet() {

		redisTemplate.delete(ZSET_TEST);
		redisTemplate.opsForZSet().add(ZSET_TEST, "a", 1);
		redisTemplate.opsForZSet().add(ZSET_TEST, "b", 2);
		redisTemplate.opsForZSet().add(ZSET_TEST, "c", 1);
		redisTemplate.opsForZSet().add(ZSET_TEST, "d", 2);
		redisTemplate.opsForZSet().add(ZSET_TEST, "e", 1);
		redisTemplate.opsForZSet().add(ZSET_TEST, "f", 3);
		redisTemplate.opsForZSet().add(ZSET_TEST, "g", 4);
		redisTemplate.opsForZSet().add(ZSET_TEST, "h", 4);
		// 找到1-3分数的数据列表
		redisTemplate.opsForZSet().rangeByScoreWithScores(ZSET_TEST, 1, 3).forEach(e -> {
			ZSetOperations.TypedTuple<String> tuple = (ZSetOperations.TypedTuple) e;
			System.out.println(tuple.getScore() + ":" + tuple.getValue());
		});

		// 找到1-3分数的数据数量
		System.out.println(redisTemplate.opsForZSet().count(ZSET_TEST, 1, 3));
	}

	// 判断某个IP是否在ZSet的范围内
	// 通过起始值先过滤掉大于指定IP的，再剩下的集合进行遍历
	public boolean isIpInRange(String ipAddress) {
		ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
		double ip = (double) convertIPToScore(ipAddress);
		Set<ZSetOperations.TypedTuple<String>> result = opsForZSet.rangeByScoreWithScores(IP_SCORE_RANGE_KEY, 0, ip);
		for (ZSetOperations.TypedTuple<String> o : result) {

			String[] arr = o.getValue().split("-");
			long start = convertIPToScore(arr[0]);
			long end = convertIPToScore(arr[1]);
			if (ip >= start && ip <= end) {
				System.out.println("find rangeL" + arr[0] + "-" + arr[1]);
				return true;
			}
		}

		return false;
	}

	private void storeIPScoreRangeToRedis(String startIP, String endIP) {
		long startScore = convertIPToScore(startIP);
		double score = Double.valueOf(startScore);
		redisTemplate.opsForZSet().add(IP_SCORE_RANGE_KEY, startIP + "-" + endIP, score);
	}

	// 不能实现通过指定IP，去zset集合找到包含这个ip的范围数据，如果只是找某个score的范围，用这个例子是可以的
	@Test
	public void testContainsRangeIp() {
		redisTemplate.delete(IP_SCORE_RANGE_KEY);
		// 存储 IP 范围到 Redis
		storeIPScoreRangeToRedis("103.159.125.66", "103.159.125.177");
		storeIPScoreRangeToRedis("114.250.19.131", "116.250.19.255");
		storeIPScoreRangeToRedis("10.10.10.131", "10.10.20.255");
		storeIPScoreRangeToRedis("192.10.10.131", "192.10.10.255");

		// 检查用户真实 IP 是否在 IP 集合中
		String userIP = "114.250.19.132";
		boolean inBlacklist = isIpInRange(userIP);

		if (inBlacklist) {
			System.out.println(userIP + " is in the IP blacklist.");
		}
		else {
			System.out.println(userIP + " is not in the IP blacklist.");
		}

	}

}
