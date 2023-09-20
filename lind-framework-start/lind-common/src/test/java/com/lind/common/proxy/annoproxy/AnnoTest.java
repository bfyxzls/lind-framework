package com.lind.common.proxy.annoproxy;

import com.lind.common.proxy.User;
import com.lind.common.proxy.staticproxy.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableDictionaryAdapter
public class AnnoTest {

	@Autowired
	UserService userService;

	@Test
	public void update() {
		// 在拦截器中为user.name赋值
		userService.update(new User());
	}

}
