package com.lind.logback.mdc;

import com.netflix.hystrix.strategy.HystrixPlugins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2023/1/30 11:46
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class HystrixCircuitBreakerConfiguration {

	@PostConstruct
	public void init() {
		if (HystrixPlugins.getInstance().getConcurrencyStrategy() == null) {
			HystrixPlugins.getInstance().registerConcurrencyStrategy(new RequestContextHystrixConcurrencyStrategy());
		}
	}

}
