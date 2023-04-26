package com.lind.common.factory;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lind
 * @date 2022/7/1 17:25
 * @description
 */
@PropertySource(value = "classpath:test.yml", factory = YamlPropertySourceFactory.class)
@Configuration
public class AutoConfig {

}
