package com.lind.common.proxy.invocation_handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lind
 * @date 2022/12/26 9:30
 * @since 1.0.0
 */
@SpringBootTest()
public class DTest {

	@Test
	public void proxy() {
		// DynaProxyHello的作用就是实现InvocationHandler接口实现代理功能，并在DynaProxyHello方法的invoke中去实现自己需要的业务功能
		DynaProxyHello helloproxy = new DynaProxyHello();
		IHello hello = new Helloimplements();
		// 将hello设置进 代理类中，实现在此作用域中Helloimplements实例hello的所有调用都会经过DynaProxyHello代理转发
		IHello ihello = (IHello) helloproxy.bind(hello);
		ihello.sayHello("Jerry");
	}

}
