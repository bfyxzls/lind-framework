package com.lind.fileupload.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lind
 * @date 2024/1/26 14:32
 * @since 1.0.0
 */
@SpringBootApplication
public class LocalGrpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalGrpcServerApplication.class, args);
	}

}
