package com.lind.common.bean.ordertest;

import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2024/4/30 10:50
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(SecondAutoConfiguration.class)
//@DependsOn("secondAutoConfiguration")//决定了注册的顺序，它在secondAutoConfiguration之后注册
public class FirstMidSecondAutoConfiguration {
    @Bean
    public String firstMidSecondDemo() {
        System.out.println("FirstMidSecondAutoConfiguration loaded.");
        return "FirstMidSecondAutoConfiguration";
    }
}
