package com.lind.common.bean.application_context_aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author lind
 * @date 2022/9/9 9:41
 * @since 1.0.0
 */
public class LindAware implements ApplicationContextAware {

	ApplicationContext applicationContext;

	LindContext lindContext;

	public void contextPrint() {
		this.lindContext.print();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		if (this.applicationContext.getBeansOfType(LindContext.class).isEmpty()) {
			throw new IllegalArgumentException("未加载或者未发现LindContext的bean，请保证它可以正常加载到Spring容器.");
		}
		this.lindContext = this.applicationContext.getBean(LindContext.class);
	}

}
