package com.lind.common.proxy.staticproxy;

import com.lind.common.proxy.User;
import org.junit.jupiter.api.Test;

/**
 * 静态代理测试
 */
public class PTest {

	@Test
	public void staticProxyTest() {
		UserService userServiceImpl = new UserServiceImpl();
		UserService proxy = new UserServiceProxy(userServiceImpl);

		proxy.select();
		proxy.update(new User());
	}

}
