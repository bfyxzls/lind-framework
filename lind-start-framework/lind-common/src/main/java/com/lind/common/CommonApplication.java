package com.lind.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
// @EnableJacksonFormatting
public class CommonApplication {

	@Autowired
	ObjectMapper objectMapper;

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	public String print() {
		System.out.println("print class loader1.1");
		return "print class loader1.1";
	}

	@GetMapping("version")
	public ResponseEntity version() {
		return ResponseEntity.ok("1.0.0");
	}

}
