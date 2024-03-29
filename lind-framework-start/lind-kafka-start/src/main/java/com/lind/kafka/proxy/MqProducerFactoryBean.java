package com.lind.kafka.proxy;

import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象 为某种类型创建动态代理.
 *
 * @param <T>
 * @author lind
 */
@Data
public class MqProducerFactoryBean<T> implements FactoryBean<T> {

	private Class<T> interfaceType;

	private BeanFactory applicationContext;

	@Override
	public T getObject() throws Exception {
		// 这里主要是创建接口对应的实例，便于注入到spring容器中
		InvocationHandler handler = new ServiceProxy<>(applicationContext);
		return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
	}

	@Override
	public Class<T> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
