package com.lind.keycloak.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class KcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KcServerApplication.class, args);
	}

}
