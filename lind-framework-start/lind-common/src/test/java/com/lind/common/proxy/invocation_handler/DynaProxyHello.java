package com.lind.common.proxy.invocation_handler;

import com.lind.common.core.util.SpringContextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lind
 * @date 2022/12/26 9:29
 * @since 1.0.0
 */
public class DynaProxyHello implements InvocationHandler {

	private Object delegate;

	public Object bind(Object delegate) {
		this.delegate = delegate;
		return Proxy.newProxyInstance(this.delegate.getClass().getClassLoader(),
				this.delegate.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			System.out.println("问候之前的日志记录...");
			SpringContextUtils.getBean(OtherBean.class).print(); // 这块不能直接用@Autowired
			// JVM通过这条语句执行原来的方法(反射机制)
			result = method.invoke(this.delegate, args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
