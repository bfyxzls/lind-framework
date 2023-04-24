package com.lind.common.proxy.staticproxy;

import com.lind.common.proxy.User;

import java.util.Date;

public class UserServiceProxy implements UserService {

	private UserService target; // 被代理的对象

	public UserServiceProxy(UserService target) {
		this.target = target;
	}

	public void select() {
		before();
		target.select(); // 这里才实际调用真实主题角色的方法
		after();
	}

	public void update(User user) {
		before();
		target.update(user); // 这里才实际调用真实主题角色的方法
		after();
	}

	private void before() { // 在执行方法之前执行
		System.out.println(String.format("log start time [%s] ", new Date()));
	}

	private void after() { // 在执行方法之后执行
		System.out.println(String.format("log end time [%s] ", new Date()));
	}

}