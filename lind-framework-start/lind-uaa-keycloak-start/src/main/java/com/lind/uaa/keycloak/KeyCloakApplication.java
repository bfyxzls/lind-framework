package com.lind.uaa.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableGlobalMethodSecurity(prePostEnabled=true)
public class KeyCloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyCloakApplication.class, args);
	}

}
