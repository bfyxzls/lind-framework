package com.lind.common.pattern;

import com.lind.common.pattern.factorymethod.Factory;
import com.lind.common.pattern.factorymethod.H2Factory;
import com.lind.common.pattern.factorymethod.UserRepository;
import com.lind.common.pattern.factorymethod.Userinfo;
import org.junit.Test;

public class FactoryTest {

	@Test
	public void userTest() {
		Factory factory = new H2Factory();
		UserRepository userRepository = factory.createUserRepository();
		userRepository.insert(new Userinfo());
	}

}
