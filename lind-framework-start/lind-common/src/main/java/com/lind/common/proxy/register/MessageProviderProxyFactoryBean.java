package com.lind.common.proxy.register;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 2.代理对象的FactoryBean.
 *
 * @param <T>
 */
@Slf4j
@Data
public class MessageProviderProxyFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceType;

	private BeanFactory beanFactory;

	@Override
	public T getObject() throws Exception {
		// 这里主要是创建接口对应的实例，便于注入到spring容器中
		InvocationHandler handler = new MessageProviderProxy(beanFactory);
		return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
