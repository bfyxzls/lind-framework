package com.lind.redis;

import com.lind.redis.limit.RedissonSlidingWindowRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RScript;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * @author lind
 * @date 2023/7/21 14:20
 * @since 1.0.0
 */
@Slf4j
public class RedissonTest {

	Config config;

	RedissonClient redisson;

	@Before
	public void init() {
		config = new Config();
		config.useSingleServer().setAddress("redis://localhost:6379");
		// 创建JsonJacksonCodec对象，并设置为Redisson的默认编码解码器，不配置的话，你的数字和字段会基于二进制存储，不方便查看
		JsonJacksonCodec codec = new JsonJacksonCodec();
		config.setCodec(codec);
		redisson = Redisson.create(config);
	}

	@Test
	public void rateLimit() {
		// 创建RRateLimiter对象，限制每秒处理3个请求
		RRateLimiter rateLimiter = redisson.getRateLimiter("myRateLimiter");
		rateLimiter.trySetRate(RateType.OVERALL, 3, 1, RateIntervalUnit.SECONDS);

		// 模拟处理请求
		for (int i = 1; i <= 5; i++) {
			if (rateLimiter.tryAcquire()) {
				System.out.println("处理请求 " + i + " 成功");
			}
			else {
				System.out.println("处理请求 " + i + " 失败，超过限流速率");
			}
		}

		// 关闭Redisson客户端连接
		redisson.shutdown();
	}

	@Test
	public void scrollWindow() throws InterruptedException {
		// 模拟处理请求
		log.debug("有吗");
		RedissonSlidingWindowRateLimiter redissonSlidingWindowRateLimiter = new RedissonSlidingWindowRateLimiter(
				"myRateLimiter", 3000, 3);
		for (int i = 1; i <= 15; i++) {
			if (redissonSlidingWindowRateLimiter.allowRequest()) {
				System.out.println("处理请求 " + i + " 成功");
				Thread.sleep(1000);
			}
			else {
				System.out.println("处理请求 " + i + " 失败，超过限流速率");
			}
		}
	}

	@Test
	public void luaSimple() {
		// 定义Lua脚本,脚本执行错误，请升级redisson版本.
		String luaScript = "return tonumber(1)";// tonumber(1)为一个long类型
		// 执行Lua脚本并获取结果
		RScript script = redisson.getScript();
		Long result = script.eval(RScript.Mode.READ_ONLY, luaScript, RScript.ReturnType.INTEGER);
		// 这个返回值如果设置integer就会出错，必须是Long
		System.out.println("返回值：" + result);

		RScript script1 = redisson.getScript();
		String lua = "local message = \"Hello, Lua!\"\n" + "return message";
		String result1 = script1.eval(RScript.Mode.READ_ONLY, lua, RScript.ReturnType.STATUS);
		System.out.println("返回值：" + result1);
		// 关闭Redisson客户端连接
		redisson.shutdown();
	}

	@Test
	public void luaStringParam() {
		// 定义Lua脚本
		String luaScript = "return  ARGV[1]";
		// 执行Lua脚本并获取结果
		RScript script = redisson.getScript();
		String result = script.eval(RScript.Mode.READ_ONLY, luaScript, RScript.ReturnType.VALUE,
				Collections.singletonList("a"), "abc");
		System.out.println("计算结果：" + result);
	}

	@Test
	public void luaCompute() throws ExecutionException, InterruptedException {
		// 定义Lua脚本
		String luaScript = "-- 在Redis中设置一个键为num1的值\n" +
				"redis.call('SET', 'num1', ARGV[1])\n" +
				"-- 在Redis中设置一个键为num2的值\n" +
				"redis.call('SET', 'num2', ARGV[2])\n" +
				"\n" +
				"-- 从Redis中获取num1和num2的值，并将它们转换为数字类型\n" +
				"local num1 = tonumber(redis.call('GET', 'num1'))\n" +
				"local num2 = tonumber(redis.call('GET', 'num2'))\n" +
				"\n" +
				"-- 如果num1和num2均为有效数字，则将它们相加并返回结果\n" +
				"if num1 ~= nil and num2 ~= nil then\n" +
				"    local sum = num1 + num2\n" +
				"    return sum\n" +
				"else\n" +
				"    return nil\n" +
				"end\n";
		// 执行Lua脚本并获取结果
		RScript script = redisson.getScript();
		RFuture<Object> future = script.evalAsync(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.INTEGER,Collections.singletonList("a"), 1,2);
		Object result = future.get();
		System.out.println("Sum: " + result);
	}


	@Test
	public void luaComputerparam2() {
		// 定义Lua脚本
		String luaScript = "return  tonumber(ARGV[1]) + tonumber(ARGV[2])"; //这块是需要配置序列化的，不配置总是为nil
		// 执行Lua脚本并获取结果
		RScript script = redisson.getScript();
		Long result = script.eval(RScript.Mode.READ_ONLY, luaScript, RScript.ReturnType.INTEGER,
				Collections.singletonList("a"), 1,3);
		System.out.println("计算结果：" + result);
	}


}
