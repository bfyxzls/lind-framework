package com.lind.common.http;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 使用简单，没有连接池概念，高并发环境下，容易造成服务器端口耗尽.
 *
 * @author lind
 * @date 2023/1/30 9:35
 * @since 1.0.0
 */
@Slf4j
public class HutoolTest {

	public static void main(String[] args) throws InterruptedException {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(100);
		taskExecutor.setMaxPoolSize(200);
		taskExecutor.setQueueCapacity(100);
		taskExecutor.setKeepAliveSeconds(60);
		taskExecutor.setThreadNamePrefix("test-");
		taskExecutor.initialize();
		CountDownLatch latch = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			taskExecutor.submit(() -> {
				log.info("{}", new String(HttpUtil.get("http://localhost:8181/s?wd=zzl").getBytes()));
				latch.countDown();
			});
		}
		latch.await();
		System.out.println("main lacthCount:" + latch.getCount());
		System.out.println("all subTask finished");
		taskExecutor.shutdown();
	}

	@Test
	public void get() {
		log.info("{}", new String(HttpUtil.get("http://localhost:8181/s?wd=zzl").getBytes()));
	}

	@Test
	public void testKcLogin() throws InterruptedException {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", "pkulaw");
		params.put("client_secret", "c0b7ab8e-485b-4a10-bff8-7c7d3f472096");
		params.put("grant_type", "password");
		params.put("username", "test");
		params.put("password", "12345678a");
		params.put("scope", "openid");
		for (int i = 0; i < 2000; i++) {
			String result = HttpUtil
					.post("http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/token", params)
					.toLowerCase();
			System.out.println("result=" + result);
			Thread.sleep(1);
		}
	}

}
