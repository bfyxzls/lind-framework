package com.lind.redis;

import com.lind.redis.annotation.RepeatSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lind
 * @date 2022/7/13 11:22
 * @since 1.0.0
 */
@RestController
public class RepeatSubmitController {

	@RepeatSubmit(expireTime = 1)
	@GetMapping("get")
	public String get() {
		return "success";
	}

}
