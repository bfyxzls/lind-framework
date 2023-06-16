package com.lind.common;

import com.lind.common.exception.LindException;
import com.lind.common.locale.LocaleMessageUtils;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
@Slf4j
@EnableAsync
// @EnableJacksonFormatting
public class CommonApplication {

	@Autowired
	TestService testService;

	HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 16);

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

	@GetMapping("async")
	public ResponseEntity async() throws InterruptedException {
		testService.hello();
		return ResponseEntity.ok("ok");
	}

	/**
	 * 进行客户端的校验，并返回错误信息，于统一异常进行捕获.
	 * @return
	 * @throws InterruptedException
	 */
	@GetMapping("exception")
	public ResponseEntity exception(String userName) throws InterruptedException {
		Optional.ofNullable(userName).orElseThrow(() -> new LindException("用户名不能为空", "1001"));
		Optional.ofNullable(userName).ifPresent(o -> {
			if (o.length() < 5) {
				throw new LindException("用户名长度不能小于5", "1002");
			}
			if (o.equals("zzl")) {
				throw new LindException("用户名zzl已存在", "1003");
			}
		});
		// com.lind.common.rest.ResponseBodyAdvice 为它添加了json格式化
		return ResponseEntity.ok("测试通过");

	}

	@GetMapping("create-order")
	public ResponseEntity createOrder(String username) {
		timer.newTimeout((timeout) -> {
			log.info("{}，您在一分钟之内，您没有付款，订单已被关闭.", username);
		}, 1000 * 60, TimeUnit.MILLISECONDS);
		return ResponseEntity.ok(username + "建立了一个订单");
	}

}
