package com.lind.hibernate.controller;

import com.lind.hibernate.feign.ServerClient;
import com.lind.hibernate.service.JwtService;
import com.lind.hibernate.service.KeycloakRestService;
import com.lind.redis.util.RedisRateLimiterPolice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *     com.edw.controller.StudentController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com > 17 Agt 2020 21:15
 */
@RestController
@RequestMapping("rest")
public class IndexController {

	@Autowired
	ServerClient serverClient;

	@Autowired
	RedisRateLimiterPolice redisRateLimiter;

	private Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private KeycloakRestService restService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/index")
	public ResponseEntity index() {
		return ResponseEntity.ok("hello");
	}

	@GetMapping("/server")
	public ResponseEntity server() {
		return ResponseEntity.ok(serverClient.index());
	}

}
