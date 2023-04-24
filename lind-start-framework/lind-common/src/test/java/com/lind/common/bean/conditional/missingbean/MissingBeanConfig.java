package com.lind.common.bean.conditional.missingbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MissingBeanConfig {

	/**
	 * @ConditionalOnMissingBean当没有其它MissingBean的bean时，就使用DefaultMissingBean这个bean. @return
	 */
	@Bean
	@ConditionalOnMissingBean(MissingBean.class)
	public MissingBean defaultMissingBean() {
		return new DefaultMissingBean();
	}

}
