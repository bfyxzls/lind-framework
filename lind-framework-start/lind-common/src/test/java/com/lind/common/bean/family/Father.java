package com.lind.common.bean.family;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2022/8/18 11:07
 * @since 1.0.0
 */
@Configuration
public class Father {

	@Bean
	public String fatherTest() {
		System.out.println("配置類FatherConfig構造器被執行");
		return "配置類FatherConfig構造器被執行...";
	}

}
