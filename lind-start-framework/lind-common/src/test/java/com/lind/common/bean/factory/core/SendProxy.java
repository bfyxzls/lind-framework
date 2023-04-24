package com.lind.common.bean.factory.core;

import lombok.Data;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler用来生成代理对象 BeanFactory是bean的工厂，用它的getBean()方法来获取bean的实例
 */
@Data
public class SendProxy implements InvocationHandler {

	private BeanFactory applicationContext;

	public SendProxy(BeanFactory applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("SendProxy...");
		if (method.getName().equals("insert")) {
			System.out.println("insert...");

		}
		if (method.getName().equals("delete")) {
			System.out.println("delete...");

		}
		return null;
	}

}
