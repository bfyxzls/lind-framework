package com.lind.common.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态代理. 为方法添加preAction和postAction事件
 */
public class SubjectProxyHandler implements InvocationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SubjectProxyHandler.class);

	private Object target;

	@SuppressWarnings("rawtypes") // 忽略某种警告
	public SubjectProxyHandler(Class clazz) {
		try {
			this.target = clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException ex) {
			LOG.error("Create proxy for {} failed", clazz.getName());
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		preAction();
		Object result = method.invoke(target, args);
		postAction();
		LOG.info("Proxy class name {}", proxy.getClass().getName());
		return result;
	}

	private void preAction() {
		LOG.info("SubjectProxyHandler.preAction()");
	}

	private void postAction() {
		LOG.info("SubjectProxyHandler.postAction()");
	}

}