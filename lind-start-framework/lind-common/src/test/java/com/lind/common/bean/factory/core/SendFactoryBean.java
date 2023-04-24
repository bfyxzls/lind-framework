package com.lind.common.bean.factory.core;

import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 实现工厂bean接口，重写getObject()方法来生产一个bean对象.
 *
 * @param <T>
 */
@Data
public class SendFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceType;

	private BeanFactory beanFactory;

	@Override
	public T getObject() throws Exception {
		InvocationHandler handler = new SendProxy(beanFactory);
		return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

}
