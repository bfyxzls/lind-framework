package com.lind.common.bean.applicationcontextaware_init_disposable;

import cn.hutool.core.lang.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class LindFactory implements ApplicationContextAware, InitializingBean, DisposableBean {

	@Override
	public void destroy() throws Exception {
		System.err.println("销毁bean");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("bean初始化之后");

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Assert.isTrue(applicationContext != null);

	}

	public void start() {
		System.out.println("start");
	}

}
