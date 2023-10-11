package com.lind.common.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author lind
 * @date 2023/6/20 17:03
 * @since 1.0.0
 */
public class CglibProxyUserService implements MethodInterceptor {

	public Object newInstall(Object object) {
		return Enhancer.create(object.getClass(), this);
	}

	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("我被CglibProxy代理了");
		return methodProxy.invokeSuper(o, objects);
	}

}
