package com.lind.logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
public class SpELTest {

	@Autowired
	UserService userService;

	@Test
	public void test() {
		User user = new User();
		user.setAddr("beijing");
		user.setName("zzl");
		userService.insert(user);
	}

}
