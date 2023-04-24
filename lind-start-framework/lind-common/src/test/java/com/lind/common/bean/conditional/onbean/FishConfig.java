package com.lind.common.bean.conditional.onbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FishConfig {

	/**
	 * @return @ConditionalOnBean我觉得它是一个依赖，即FishFood的注入，它依赖于是否有Fishing这个bean，如果有Fishing，就可以注册FishFood.
	 */
	@Bean
	@ConditionalOnBean(Fishing.class)
	public FishFood fish1() {
		return new Fish1();
	}

	public class Fish1 implements FishFood {

		public void hello() {
			System.out.println("FishFood依赖类型Fishing");
		}

	}

}
