package com.lind.common.bean.family;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2022/8/18 11:07
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(Father.class) // 在爸爸之初始化
public class Son {

	@Bean
	public String SonBean() {
		System.out.println("配置類SonConfig構造器被執行...");
		return null;
	}

}
