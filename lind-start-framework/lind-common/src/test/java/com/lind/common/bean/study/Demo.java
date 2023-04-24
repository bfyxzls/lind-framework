package com.lind.common.bean.study;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Demo {

	@Autowired
	FactoryBeanService factoryBeanService;

	@Test
	public void test() {
		ApplicationContext context = new AnnotationConfigApplicationContext(FactoryBeanLearnConfig.class);
		context.getBean(FactoryBeanService.class).testFactoryBean();
	}

	@Test
	public void proxyTest() {
		CatProxy catProxy = new CatProxy();
		ICat cat = catProxy.getProxy();
		cat.run();
	}

	interface ICat {

		void run();

	}

	/**
	 * 装饰者模式,和代理模式很类似. 1.装饰者和被装饰者实现或继承相同的接口或类 2.装饰者持有被装饰者的引用
	 */
	static class Decorate implements ICat {

		ICat mICat;

		public Decorate(ICat ICat) {
			this.mICat = ICat;
		}

		@Override
		public void run() {
			System.out.println("猫抓老鼠");
			mICat.run();
		}

	}

	static class Cat implements ICat {

		@Override
		public void run() {
			System.out.println("猫抓到了老鼠...");
		}

	}

	// 实现InvocationHandler接口,实现invoke方法
	public class CatProxy implements InvocationHandler {

		final ICat cat = new Cat();

		public ICat getProxy() {
			return (ICat) Proxy.newProxyInstance(getClass().getClassLoader(), cat.getClass().getInterfaces(), this);
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			if (method.getName().equals("run")) {
				System.out.println("代理cat");
				return method.invoke(cat, args);
			}
			return null;
		}

	}

}
