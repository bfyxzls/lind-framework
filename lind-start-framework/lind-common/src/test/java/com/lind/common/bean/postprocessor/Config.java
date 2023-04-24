package com.lind.common.bean.postprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ComponentScan注解就开启了自动扫描，如果注解没有写括号里面的东西，@ComponentScan默认会扫描与配置类相同的包 它会将当前包下声明为@Component的对象注册到IOC容器里
 */
@Configuration
@ComponentScan
public class Config {

	@Bean
	public Context context() {
		return new Context();
	}

	@Bean
	public Init init() {
		return new Init();
	}

}
