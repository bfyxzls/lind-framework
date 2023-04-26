package com.lind.feign;

import com.netflix.hystrix.strategy.HystrixPlugins;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2023/1/30 11:46
 * @since 1.0.0
 */
@Slf4j
public class HystrixCircuitBreakerConfiguration {

	@PostConstruct
	public void init() {
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new RequestContextHystrixConcurrencyStrategy());
	}

}
