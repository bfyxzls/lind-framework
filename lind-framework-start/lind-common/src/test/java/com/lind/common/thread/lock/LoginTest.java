package com.lind.common.thread.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/11/11 8:53
 * @since 1.0.0
 */
public class LoginTest {

	static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(10, 100, 10, TimeUnit.SECONDS,
			new SynchronousQueue<>());

	public static void main(String[] args) {
		LoginService loginService = new LoginService();
		List<CompletableFuture<String>> futureList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			CompletableFuture<String> completedFuture = CompletableFuture.supplyAsync(loginService::getData,
					EXECUTOR_SERVICE);
			futureList.add(completedFuture);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[] {})).join();
	}

}
