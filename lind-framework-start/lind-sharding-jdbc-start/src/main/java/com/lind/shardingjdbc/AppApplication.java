package com.lind.shardingjdbc;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.lind.shardingjdbc.entity.Order;
import com.lind.shardingjdbc.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lind
 * @date 2022/11/15 11:28
 * @since 1.0.0
 */
@SpringBootApplication
@RestController
public class AppApplication {

	@Autowired
	OrderMapper orderMapper;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@GetMapping("insert")
	@DS("sharding")
	public ResponseEntity test() {
		Order order = new Order();
		order.setAmount(100);
		order.setUserId(1);
		orderMapper.insert(order);
		return ResponseEntity.ok("success");
	}

	@GetMapping("insert-not-sharding")
	public ResponseEntity testNotSharding() {
		Order order = new Order();
		order.setAmount(101);
		order.setUserId(2);
		orderMapper.insert(order);
		return ResponseEntity.ok("success");
	}

}
