package com.lind.common.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lind
 * @date 2022/11/11 8:50
 * @since 1.0.0
 */
public class LoginService {

	private static String token;

	private static Lock lockObj = new ReentrantLock();

	public void login() throws InterruptedException {
		Thread.sleep(1000);
		token = "abc123";
		System.out.println("登录");

	}

	public String getData() {
		try {
			Thread.sleep(1000);
			if (token == null) {
				if (lockObj.tryLock()) {
					login();
					lockObj.unlock();
				}
				else {
					lockObj.lock();
					lockObj.unlock();
				}
				return getData();

			}
		}
		catch (InterruptedException ex) {
		}
		System.out.println("用户数据");
		return "用户数据";
	}

}
