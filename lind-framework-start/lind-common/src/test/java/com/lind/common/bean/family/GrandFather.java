package com.lind.common.bean.family;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2022/8/18 11:07
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore(Father.class) // 在我儿子Father之前，我GrandFather先初始化
public class GrandFather {

	@Bean
	public String grandFatherBean() {
		System.out.println("配置類GrandFatherConfig構造器被執行...");
		return null;
	}

}
