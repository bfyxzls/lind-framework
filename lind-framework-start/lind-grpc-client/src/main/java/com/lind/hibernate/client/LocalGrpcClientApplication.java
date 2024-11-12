package com.lind.hibernate.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Michael (yidongnan@gmail.com)
 * @since 2016/11/8
 */
@SpringBootApplication
public class LocalGrpcClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalGrpcClientApplication.class, args);
	}

}
