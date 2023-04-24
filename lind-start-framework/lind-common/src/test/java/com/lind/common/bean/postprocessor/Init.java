package com.lind.common.bean.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

public class Init implements BeanPostProcessor {

	@Autowired
	Context context;

	@Nullable
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof AbstractSend) {
			context.dic.put(bean.toString(), (AbstractSend) bean);
		}
		return bean;
	}

}
