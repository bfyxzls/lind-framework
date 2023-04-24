package com.lind.common.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EatConfig {

	@Bean
	@ConditionalOnMissingBean(Eat.class)
	public Eat test() {
		return new DefaultEat();

	}

}
