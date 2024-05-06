package com.lind.common.bean.ordertest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2024/4/30 10:50
 * @since 1.0.0
 */
@Configuration
public class FirstAutoConfiguration {

    @Bean("firstBean")
    public String firstBean() {
        System.out.println("FirstAutoConfiguration loaded.");
        return "FirstBean";
    }
}
