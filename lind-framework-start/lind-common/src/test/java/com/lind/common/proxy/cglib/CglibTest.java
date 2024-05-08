package com.lind.common.proxy.cglib;

import com.lind.common.proxy.staticproxy.UserServiceImpl;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/6/20 17:04
 * @since 1.0.0
 */
public class CglibTest {

	@Test
	public void test_proxy_cglib() {
		CglibProxyUserService cglibProxyUserService = new CglibProxyUserService();
		// cglib可以直接代理具体类，而jdk的原生代理只能代码接口
		UserServiceImpl userService = (UserServiceImpl) cglibProxyUserService.newInstall(new UserServiceImpl());
		userService.select();
	}

}
