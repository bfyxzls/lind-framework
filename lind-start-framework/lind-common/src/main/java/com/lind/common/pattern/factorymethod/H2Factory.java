package com.lind.common.pattern.factorymethod;

public class H2Factory implements Factory {

	@Override
	public UserRepository createUserRepository() {
		return new H2UserRepository();
	}

}
