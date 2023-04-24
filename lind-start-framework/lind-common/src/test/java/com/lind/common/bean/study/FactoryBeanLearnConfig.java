package com.lind.common.bean.study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册factoryBean.
 */
@Configuration
public class FactoryBeanLearnConfig {

	@Bean
	public FactoryBeanLearn factoryBeanLearn() {// 这里的方法名称是Department类Bean的ID
		FactoryBeanLearn factoryBeanLearn = new FactoryBeanLearn();
		return factoryBeanLearn;
	}

}
