package com.lind.keycloak.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lind
 * @date 2024/5/31 16:52
 * @since 1.0.0
 */
@RestController
public class UserController {

	@GetMapping("index")
	public ResponseEntity index() throws InterruptedException {
		return ResponseEntity.ok("Hello World");
	}

}
