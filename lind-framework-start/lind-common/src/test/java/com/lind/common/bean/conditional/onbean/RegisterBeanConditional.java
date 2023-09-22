package com.lind.common.bean.conditional.onbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2023/9/20 21:42
 * @since 1.0.0
 */
@Configuration
public class RegisterBeanConditional {

	@Bean
	@ConditionalOnBean(Fishing.class) // 在这个bean的生产过程中，它依赖了Fishing这个bean.
	public Cat cat(Fishing fishing) {
		return new Cat(fishing);
	}

}
