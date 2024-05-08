package com.lind.common.bean.ordertest;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2024/4/30 10:50
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(FirstAutoConfiguration.class)
public class SecondAutoConfiguration {

	@Bean
	public String secondAutoDemo() {
		System.out.println("SecondAutoConfiguration loaded.");
		return "SecondAutoConfiguration";
	}

}
