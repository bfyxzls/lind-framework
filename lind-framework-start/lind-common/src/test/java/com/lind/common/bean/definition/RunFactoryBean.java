package com.lind.common.bean.definition;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RunFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceType;

	public Class<T> getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(Class<T> interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public T getObject() throws Exception {
		InvocationHandler handler = (proxy, method, args) -> {
			System.out.println("代理T类型做一些事情");
			return null;
		};
		return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

}
