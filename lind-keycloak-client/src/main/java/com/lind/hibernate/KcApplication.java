package com.lind.hibernate;

import com.lind.feign.annotation.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableCaching
@EnableFeignClients
public class KcApplication {

	public static void main(String[] args) {
		SpringApplication.run(KcApplication.class, args);
	}

}
