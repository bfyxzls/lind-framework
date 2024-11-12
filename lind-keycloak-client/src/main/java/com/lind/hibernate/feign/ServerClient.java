package com.lind.hibernate.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lind
 * @date 2024/5/31 16:54
 * @since 1.0.0
 */
@FeignClient("http://localhost:9091")
public interface ServerClient {

	@GetMapping("index")
	public String index();

}
