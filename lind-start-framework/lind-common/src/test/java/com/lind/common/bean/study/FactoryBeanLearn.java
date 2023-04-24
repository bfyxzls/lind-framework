package com.lind.common.bean.study;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 建立一个bean对象
 */
@Slf4j
public class FactoryBeanLearn implements FactoryBean {

	@Override
	public Object getObject() throws Exception {
		return new FactoryBeanServiceImpl();
	}

	@Override
	public Class<?> getObjectType() {
		return FactoryBeanService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
