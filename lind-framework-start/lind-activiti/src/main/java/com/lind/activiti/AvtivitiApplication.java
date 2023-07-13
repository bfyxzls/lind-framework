package com.lind.activiti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { org.activiti.spring.boot.SecurityAutoConfiguration.class }) // @EnableRunTime
public class AvtivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvtivitiApplication.class, args);
	}

}
