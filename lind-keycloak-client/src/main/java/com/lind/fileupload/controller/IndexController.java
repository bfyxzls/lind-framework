package com.lind.fileupload.controller;

import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.fileupload.feign.ServerClient;
import com.lind.fileupload.service.JwtService;
import com.lind.fileupload.service.KeycloakRestService;
import com.lind.redis.util.RedisRateLimiterPolice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
