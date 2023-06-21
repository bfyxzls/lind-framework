package com.lind.common.proxy.cglib;

import com.lind.common.proxy.staticproxy.UserService;
import com.lind.common.proxy.staticproxy.UserServiceImpl;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/6/20 17:04
 * @since 1.0.0
 */
public class CglibTest {

	@Test
	public void test_proxy_cglib() {
		CglibProxy cglibProxy = new CglibProxy();
		UserService userService = (UserService) cglibProxy.newInstall(new UserServiceImpl());
		userService.select();
	}

}
