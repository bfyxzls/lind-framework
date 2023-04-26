package com.lind.common.proxy.staticproxy;

import com.lind.common.proxy.User;
import com.lind.common.proxy.annoproxy.EnableDictionaryAdapter;
import com.lind.common.proxy.annoproxy.DictionaryAdapterMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableDictionaryAdapter
public class UserServiceImpl implements UserService {

	public void select() {
		System.out.println("查询 selectById");
	}

	@DictionaryAdapterMethod
	public void update(User user) {
		System.out.println("更新 update");
		log.info("user:{}", user);
	}

}
