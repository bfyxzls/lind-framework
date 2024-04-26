package com.lind.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author lind
 * @date 2024/4/23 10:25
 * @since 1.0.0
 */
@SpringBootApplication()
public class CommonApp {

	public static void main(String[] args) {
		SpringApplication.run(CommonApp.class, args);
	}

}
