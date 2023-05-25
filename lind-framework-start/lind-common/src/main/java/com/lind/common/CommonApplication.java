package com.lind.common;

import com.lind.common.locale.LocaleMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@Slf4j
// @EnableJacksonFormatting
public class CommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@GetMapping("print")
	public ResponseEntity print(@RequestParam LocalDateTime date, @RequestParam Date simple) {
		Map<String, Object> result = new HashMap<>();
		result.put("time", date);
		result.put("simple", simple);
		return ResponseEntity.ok(result);
	}

	@GetMapping("version")
	public ResponseEntity version() {
		return ResponseEntity.ok("1.0.0");
	}

	@GetMapping("get-title")
	public ResponseEntity title() {
		return ResponseEntity.ok(LocaleMessageUtils.getMessage("title"));
	}

}
